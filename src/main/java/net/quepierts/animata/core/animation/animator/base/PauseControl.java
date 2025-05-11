package net.quepierts.animata.core.animation.animator.base;

import org.jetbrains.annotations.Nullable;

/**
 * Animation pause control
 * @param <TTarget> identity reference type
 */
@SuppressWarnings("unused")
public interface PauseControl<TTarget> {
    /**
     * Pause the animation
     * @param key identity reference of animation, null for default animation
     */
    void pause(@Nullable TTarget key);

    /**
     * Resume the animation
     * @param key identity reference of animation, null for default animation
     */
    void resume(TTarget key);

    /**
     * Check if the animation is paused
     * @param key identity reference of animation, null for default animation
     * @return true if paused
     */
    boolean isPaused(TTarget key);
}
