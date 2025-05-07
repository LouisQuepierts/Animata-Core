package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.property.Property;

@FunctionalInterface
public interface AnimationCacheRegistrar {
    RegisterResult register(String pParent, String pName, Property pProperty);
}
