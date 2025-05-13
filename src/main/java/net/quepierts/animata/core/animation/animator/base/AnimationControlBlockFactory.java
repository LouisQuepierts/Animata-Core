package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.animator.control.AnimationControlBlock;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AnimationControlBlockFactory<TAnimator extends Animator<TTarget, TAnimation>, TTarget, TAnimation> {
    AnimationControlBlock create(
            @NotNull TAnimator pAnimator,
            @NotNull TTarget pKey,
            @NotNull TAnimation pAnimation
    );
}
