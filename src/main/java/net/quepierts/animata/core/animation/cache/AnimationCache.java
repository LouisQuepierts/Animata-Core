package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.cache.node.NamespaceNode;
import org.jetbrains.annotations.NotNull;

public interface AnimationCache {
    String INPUT_NAMESPACE = "input";
    String RUNTIME = "runtime";

    RegisterResult register(String pName, AnimationCacheNode pNode);

    RegisterResult register(String pParent, String pName, AnimationCacheNode pNode);

    RegisterResult registerNamespaced(String pNamespace, String pName, AnimationCacheNode pNode);

    void reset();

    void apply();

    void freezeRegistry();

    boolean isRegistryFrozen();

    AnimationCacheNode getCacheNode(String pPath);

    @NotNull NamespaceNode getTransientDomain(String pName);

    void addTransientNode(String pDomain, String pName, AnimationCacheNode pNode);

    AnimationCacheNode getTransientNode(String pDomain, String pName);

    void dispose(String pRuntimeDomain);
}
