package net.quepierts.animata.core.animation.cache;

import lombok.Getter;
import net.quepierts.animata.core.animation.cache.node.NamespaceNode;
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

    public RegisterResult register(String pName, AnimationCacheNode pNode) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        if (pName == null || pName.isBlank()) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_PATH, "failed.empty_name");
        }

        String namespace = this.getNamespace(pName);
        String name = this.getName(pName);
        return this.registerNamespaced(namespace, name, pNode);
    }

    public RegisterResult register(String pParent, String pName, AnimationCacheNode pNode) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        AnimationCacheNode parent = this.getNode(pParent);

        if (!(parent instanceof ChildrenContained node)) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_PATH, "failed.unknown_parent");
        }

        RegisterResult result = this.register(pName, pNode);

        if (result.success()) {
            String name = this.getName(pName);
            node.addChild(name, pNode);
        }

        return result;
    }

    public RegisterResult registerNamespaced(String pNamespace, String pName, AnimationCacheNode pNode) {
        if (this.registryFrozen) {
            return new RegisterResult(null, RegisterStatus.ILLEGAL_OPERATION, "failed.registry_frozen");
        }

        if (pNamespace == null || pNamespace.isBlank()) {
            return new RegisterResult(pNode, RegisterStatus.ILLEGAL_PATH, "failed.empty_name");
        }

        NamespaceNode namespace = this.namespaces.computeIfAbsent(pNamespace, NamespaceNode::new);
        AnimationCacheNode registered = namespace.getChild(pName);

        if (registered != null) {
            if (registered.getClass() != pNode.getClass()) {
                return new RegisterResult(registered, RegisterStatus.DUPLICATED_CONFLICT, "failed.duplicated");
            } else {
                return new RegisterResult(registered, RegisterStatus.DUPLICATED_SAME, "failed.duplicated");
            }
        }

        namespace.addChild(pName, pNode);
        return new RegisterResult(pNode, RegisterStatus.SUCCESS, "success");
    }

    @Override
    public AnimationCacheNode getCacheNode(String pPath) {
        if (pPath == null || pPath.isBlank()) return null;

        String[] names = pPath.split("\\.");
        AnimationCacheNode node = this.getNode(names[0]);

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
    public void reset() {

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

    private @Nullable AnimationCacheNode getNode(@NotNull String pPath) {
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
