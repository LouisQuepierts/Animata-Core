package net.quepierts.animata.core.animation.cache;

public interface AnimationCache {
    RegisterResult register(String pName, AnimationCacheNode pNode);

    RegisterResult register(String pParent, String pName, AnimationCacheNode pNode);

    RegisterResult registerNamespaced(String pNamespace, String pName, AnimationCacheNode pNode);

    void reset();

    void apply();

    void freezeRegistry();

    boolean isRegistryFrozen();

    AnimationCacheNode getCacheNode(String pPath);

    record RegisterResult(
            boolean success,
            AnimationCacheNode node
    ) {}
}
