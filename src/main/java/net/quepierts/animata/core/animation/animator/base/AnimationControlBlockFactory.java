package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.handle.AnimationControlBlock;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AnimationControlBlockFactory<TAnimator extends Animator<TKey, TAnimation>, TKey, TAnimation> {
    AnimationControlBlock<TKey, TAnimation> create(
            @NotNull TAnimator pAnimator,
            @NotNull TKey pKey,
            @NotNull TAnimation pAnimation
    );
}
