package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.AnimationControlBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Animation play control
 * @param <K> the identity key type used to distinguish animation control blocks
 * @param <T> the animation type (e.g., AnimationClip or AnimationSequence)
 */
@SuppressWarnings("unused")
public interface PlayControl<K, T> {
    /**
     * Play animation
     * @param pKey identity reference of animation, null to play default animation
     * @param pAnimation the animation to play
     * @return the animation control block
     */
    AnimationControlBlock<K, T> play(@Nullable K pKey, @NotNull T pAnimation);

    /**
     * Stop animation
     * @param pKey identity reference of animation, null to stop default animation
     */
    void stop(@Nullable K pKey);
}
