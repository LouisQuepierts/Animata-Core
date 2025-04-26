package net.quepierts.animata.core.animation.cache;

import org.jetbrains.annotations.Contract;

public interface IAnimationCacheFactory {
    @Contract("-> new")
    IAnimationCache createAnimationCache();
}
