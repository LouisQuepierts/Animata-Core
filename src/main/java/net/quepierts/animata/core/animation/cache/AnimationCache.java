package net.quepierts.animata.core.animation.cache;

public interface AnimationCache {
    void register(String pName, AnimationCacheNode pNode);

    void register(String pParent, String pName, AnimationCacheNode pNode);

    void registerNamespaced(String pNamespace, String pName, AnimationCacheNode pNode);

    void reset();

    void apply();

    AnimationCacheNode getCacheNode(String pPath);
}
