package net.quepierts.animata.core.animation.cache;

public interface IAnimationCache {
    void register(String pName, IAnimationCacheNode pNode);

    void register(String pParent, String pName, IAnimationCacheNode pNode);

    void registerNamespaced(String pNamespace, String pName, IAnimationCacheNode pNode);

    void reset();

    void apply();

    IAnimationCacheNode getCacheNode(String pPath);
}
