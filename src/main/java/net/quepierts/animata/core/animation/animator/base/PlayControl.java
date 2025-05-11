package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.animator.control.AnimationHandle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Animation play control
 * @param <TTarget> the identity reference type used to distinguish animation control blocks
 * @param <TAnimation> the animation type (e.g., AnimationClip or AnimationSequence)
 */
@SuppressWarnings("unused")
public interface PlayControl<TTarget, TAnimation> {
    /**
     * Play animation
     *
     * @param pKey       identity reference of animation, null to play default animation
     * @param pAnimation the animation to play
     * @return the animation control block
     */
    AnimationHandle<TTarget, TAnimation> play(@Nullable TTarget pKey, @NotNull TAnimation pAnimation);

    /**
     * Stop animation
     * @param pKey identity reference of animation, null to stop default animation
     */
    void stop(@Nullable TTarget pKey);
}
