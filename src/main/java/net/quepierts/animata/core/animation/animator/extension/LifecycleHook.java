package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.base.Animator;
import net.quepierts.animata.core.animation.animator.base.ExtensibleAnimator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LifecycleHook<TAnimator extends ExtensibleAnimator<TTarget, TAnimation>, TTarget, TAnimation>
        extends Extension {

    /**
     * Called when the animation is played
     *
     * @param pAnimator  the animator
     * @param pKey       the reference key
     * @param pAnimation the animation
     */
    default void onPlay(
            @NotNull TAnimator pAnimator,
            @Nullable TTarget pKey,
            @NotNull TAnimation pAnimation
    ) {}

    /**
     * Called when the animation is stopped
     *
     * @param pAnimator the animator
     * @param pKey      the reference key
     */
    default void onStop(
            @NotNull TAnimator pAnimator,
            @Nullable TTarget pKey
    ) {}

    /**
     * Called when the animation is finished
     *
     * @param animator the animator
     * @param pKey     the reference key
     */
    default void onFinished(
            @NotNull TAnimator animator,
            @Nullable TTarget pKey
    ) {}

    // not so far to use
    /*default void onPause(
            @NotNull TAnimator animator,
            @Nullable TTarget pKey
    ) {}

    default void onResume(
            @NotNull TAnimator animator,
            @Nullable TTarget pKey
    ) {}*/
}
