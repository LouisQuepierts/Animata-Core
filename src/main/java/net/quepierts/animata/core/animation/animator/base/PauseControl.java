package net.quepierts.animata.core.animation.animator.base;

import org.jetbrains.annotations.Nullable;

/**
 * Animation pause control
 * @param <K> identity reference type
 */
@SuppressWarnings("unused")
public interface PauseControl<K> {
    /**
     * Pause the animation
     * @param key identity reference of animation, null for default animation
     */
    void pause(@Nullable K key);

    /**
     * Resume the animation
     * @param key identity reference of animation, null for default animation
     */
    void resume(K key);

    /**
     * Check if the animation is paused
     * @param key identity reference of animation, null for default animation
     * @return true if paused
     */
    boolean isPaused(K key);
}
