package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.Animator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LifecycleHook<TAnimator extends Animator<TKey, TAnimation>, TKey, TAnimation>
        extends Extension {

    default void onPlay(
            @NotNull TAnimator pAnimator,
            @Nullable TKey pKey,
            @NotNull TAnimation pAnimation
    ) {}

    default void onStop(
            @NotNull TAnimator pAnimator,
            @Nullable TKey pKey
    ) {}

    default void onPause(
            @NotNull TAnimator animator,
            @Nullable TKey pKey
    ) {}

    default void onResume(
            @NotNull TAnimator animator,
            @Nullable TKey pKey
    ) {}
}
