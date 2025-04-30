package net.quepierts.animata.core.animation.cache;

@FunctionalInterface
public interface AnimationCacheRegistrar {
    AnimationCache.RegisterResult register(String pParent, String pName, AnimationCacheNode pNode);
}
