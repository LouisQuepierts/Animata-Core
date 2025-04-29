package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.cache.node.NamespaceNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GenericAnimationCache implements AnimationCache {
    public static final String NAMESPACE_MARK  = "::";

    private final Map<String, NamespaceNode> namespaces = new HashMap<>();
    private final String userDomain;

    public GenericAnimationCache(String useDomain) {
        this.userDomain = useDomain;
        NamespaceNode user = new NamespaceNode(useDomain);
        this.namespaces.put(useDomain, user);
    }

    public void register(String pName, AnimationCacheNode pNode) {
        if (pName == null) return;

        String namespace = this.getNamespace(pName);
        String name = this.getName(pName);
        this.registerNamespaced(namespace, name, pNode);
    }

    public void register(String pParent, String pName, AnimationCacheNode pNode) {
        AnimationCacheNode parent = this.getNode(pParent);
        this.register(pName, pNode);
        if (parent instanceof ChildrenContained node) {
            String name = this.getName(pName);
            node.addChild(name, pNode);
        }
    }

    public void registerNamespaced(String pNamespace, String pName, AnimationCacheNode pNode) {
        if (pNamespace == null || pNamespace.isBlank()) return;
        NamespaceNode namespace = this.namespaces.computeIfAbsent(pNamespace, NamespaceNode::new);
        namespace.addChild(pName, pNode);
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
