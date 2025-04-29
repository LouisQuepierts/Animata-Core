package net.quepierts.animata.core.animation.cache;

import org.jetbrains.annotations.Contract;

public interface AnimationCacheFactory {
    @Contract("-> new")
    AnimationCache createAnimationCache();
}
