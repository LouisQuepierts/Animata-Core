package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;

@SuppressWarnings({"unused", "rawtypes"})
public interface AnimatorExtension<TAnimator extends Animator<TKey, TAnimation>, TKey, TAnimation>
        extends Extension {
    default void onRegister(TAnimator pAnimator, AnimationCacheRegistrar pCacheRegistrar) {}
}
