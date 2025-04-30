package net.quepierts.animata.core.animation.cache;

import org.jetbrains.annotations.Contract;

public interface AnimationCacheProvider {
    @Contract("-> new")
    AnimationCache createAnimationCache();
}
