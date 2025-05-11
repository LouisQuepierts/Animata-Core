package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.base.ExtensibleAnimator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;

@SuppressWarnings({"unused"})
public interface AnimatorExtension<TTarget, TAnimation>
        extends Extension {
    default void onRegister(ExtensibleAnimator<TTarget, TAnimation> pAnimator, AnimationCacheRegistrar pCacheRegistrar) {}
}
