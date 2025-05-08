package net.quepierts.animata.core.animation.extension;

import net.quepierts.animata.core.animation.animator.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;

@SuppressWarnings({"unused", "rawtypes"})
public interface AnimatorExtension<TAnimator extends Animator<TKey, TAnimation>, TKey, TAnimation>
        extends Comparable<AnimatorExtension> {
    default void onRegister(TAnimator pAnimator, AnimationCacheRegistrar pCacheRegistrar) {}

    default void onPlay(TAnimator pAnimator, TAnimation pAnimationSequence, float pGlobalTime) {}

    default void onPreUpdate(TAnimator pAnimator, float pGlobalTime) {}

    default void onPostUpdate(TAnimator pAnimator, float pGlobalTime) {}

    default void onPreApply(TAnimator pAnimator) {}

    default void onPostApply(TAnimator pAnimator) {}

    default void onStop(TAnimator pAnimator) {}

    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(AnimatorExtension o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
