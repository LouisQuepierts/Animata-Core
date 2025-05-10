package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.Animator;
import org.jetbrains.annotations.NotNull;

public interface ProcessHook<TAnimator extends Animator<?, ?>> extends Extension {
    default void onPreProcess(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}

    default void onPostProcess(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}
}
