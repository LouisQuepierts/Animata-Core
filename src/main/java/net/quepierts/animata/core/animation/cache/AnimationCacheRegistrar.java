package net.quepierts.animata.core.animation.cache;

@FunctionalInterface
public interface AnimationCacheRegistrar {
    RegisterResult register(String pParent, String pName, Property pProperty);
}
