package net.quepierts.animata.core.animation.extension;

import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;

public interface AnimatorExtension extends Comparable<AnimatorExtension> {
    default void onRegister(Animator pAnimator, AnimationCacheRegistrar pCacheRegistrar) {}

    default void onPlay(Animator pAnimator, AnimationSequence pAnimationSequence, float pGlobalTime) {}

    default void onPreUpdate(Animator pAnimator, float pGlobalTime) {}

    default void onPostUpdate(Animator pAnimator, float pGlobalTime) {}

    default void onPreApply(Animator pAnimator) {}

    default void onPostApply(Animator pAnimator) {}

    default void onStop(Animator pAnimator) {}

    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(AnimatorExtension o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
