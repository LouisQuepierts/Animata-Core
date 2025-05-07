package net.quepierts.animata.core.animation.cache;

import lombok.Getter;
import net.quepierts.animata.core.property.ChildrenContained;
import net.quepierts.animata.core.property.NamespaceNode;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.property.Toggleable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GenericAnimationCache implements AnimationCache {
    public static final String NAMESPACE_MARK  = "::";

    private final Map<String, NamespaceNode> namespaces = new HashMap<>();
    private final String userDomain;

    @Getter
    private boolean registryFrozen = false;

    public GenericAnimationCache(String useDomain) {
        this.userDomain = useDomain;
        NamespaceNode user = new NamespaceNode(useDomain);
        this.namespaces.put(useDomain, user);
    }

    public RegisterResult register(String pName, Property pProperty) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        if (pName == null || pName.isBlank()) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_PATH, "failed.empty_name");
        }

        String namespace = this.getNamespace(pName);
        String name = this.getName(pName);
        return this.registerNamespaced(namespace, name, pProperty);
    }

    public RegisterResult register(String pParent, String pName, Property pProperty) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        Property parent = this.getNode(pParent);

        if (!(parent instanceof ChildrenContained node)) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_PATH, "failed.unknown_parent");
        }

        RegisterResult result = this.register(pName, pProperty);

        if (result.success()) {
            String name = this.getName(pName);
            node.addChild(name, pProperty);
        }

        return result;
    }

    public RegisterResult registerNamespaced(String pNamespace, String pName, Property pProperty) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        if (pNamespace == null || pNamespace.isBlank()) {
            return new RegisterResult(pProperty, RegisterStatus.ILLEGAL_PATH, "failed.empty_name");
        }

        if (pNamespace.equals(RUNTIME)) {
            return new RegisterResult(null, RegisterStatus.SYSTEM_REGISTERED, "failed.runtime_namespace");
        }
        
        NamespaceNode namespace = this.namespaces.computeIfAbsent(pNamespace, NamespaceNode::new);
        Property registered = namespace.getChild(pName);

        if (registered != null) {
            if (registered.getClass() != pProperty.getClass()) {
                return new RegisterResult(registered, RegisterStatus.DUPLICATED_CONFLICT, "failed.duplicated");
            } else {
                return new RegisterResult(registered, RegisterStatus.DUPLICATED_SAME, "failed.duplicated");
            }
        }

        namespace.addChild(pName, pProperty);
        return new RegisterResult(pProperty, RegisterStatus.SUCCESS, "success");
    }

    @Override
    public Property getCacheNode(String pPath) {
        if (pPath == null || pPath.isBlank()) return null;

        String[] names = pPath.split("\\.");
        Property node = this.getNode(names[0]);

        if (node instanceof Toggleable) {
            ((Toggleable) node).setEnabled(true);
        }

        int i = 1;
        while (i < names.length) {
            if (node == null) break;
            node = node.getChild(names[i]);

            if (node instanceof Toggleable) {
                ((Toggleable) node).setEnabled(true);
            }
            i++;
        }

        return node;
    }

    @Override
    public @NotNull NamespaceNode getTransientDomain(String pName) {
        if (pName == null || pName.isBlank()) {
            throw new IllegalArgumentException("failed.empty_name");
        }
        
        NamespaceNode runtime = this.namespaces.computeIfAbsent(AnimationCache.RUNTIME, NamespaceNode::new);
        Property domain = runtime.getChild(pName);
        if (domain instanceof NamespaceNode node) {
            return node;
        }
        NamespaceNode node = new NamespaceNode(pName);
        runtime.addChild(pName, node);
        return node;
    }

    @Override
    public RegisterResult addTransientNode(String pDomain, String pName, Property pProperty) {
        if (pDomain == null || pDomain.isBlank()) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_PATH, "failed.empty_name");
        }
        NamespaceNode domain = this.getTransientDomain(pDomain);
        Property property = domain.getChild(pName);
        if (property != null) {
            if (property.getClass() != pProperty.getClass()) {
                return new RegisterResult(property, RegisterStatus.DUPLICATED_CONFLICT, "failed.duplicated");
            } else {
                return new RegisterResult(property, RegisterStatus.DUPLICATED_SAME, "failed.duplicated");
            }
        }
        domain.addChild(pName, pProperty);
        return new RegisterResult(pProperty, RegisterStatus.SUCCESS, "success");
    }

    @Override
    public Property getTransientNode(String pDomain, String pName) {
        if (pDomain == null || pDomain.isBlank()) {
            throw new IllegalArgumentException("failed.empty_name");
        }

        if (pName == null || pName.isBlank()) {
            throw new IllegalArgumentException("failed.empty_name");
        }

        NamespaceNode domain = this.getTransientDomain(pDomain);
        return domain.getChild(pName);
    }

    @Override
    public void dispose(String pRuntimeDomain) {
        if (pRuntimeDomain == null || pRuntimeDomain.isBlank()) {
            this.namespaces.remove(RUNTIME);
        } else {
            NamespaceNode runtime = this.namespaces.get(RUNTIME);
            if (runtime != null) {
                runtime.dispose(pRuntimeDomain);
            }
        }
    }

    @Override
    public void reset() {
        NamespaceNode node = this.namespaces.get(RUNTIME);
        if (node != null) {
            node.dispose("");
        }
        this.namespaces.remove(RUNTIME);
    }

    @Override
    public void apply() {

    }

    @Override
    public void freezeRegistry() {
        if (this.registryFrozen) {
            return;
        }
        this.registryFrozen = true;
    }

    public void printRegistry() {
        for (Map.Entry<String, NamespaceNode> entry : this.namespaces.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().print();
        }
    }

    private @Nullable Property getNode(@NotNull String pPath) {
        String namespace = this.getNamespace(pPath);
        NamespaceNode namespaceNode = this.namespaces.get(namespace);

        if (namespaceNode == null) {
            return null;
        }

        String name = this.getName(pPath);
        return namespaceNode.getChild(name);
    }

    public @NotNull String getName(@NotNull String pPath) {
        int index = pPath.indexOf(NAMESPACE_MARK);
        return index == -1 ? pPath : pPath.substring(index + 2);
    }

    public @NotNull String getNamespace(@NotNull String pPath) {
        int index = pPath.indexOf(NAMESPACE_MARK);
        return index == -1 ? this.userDomain : pPath.substring(0, index);
    }
}
