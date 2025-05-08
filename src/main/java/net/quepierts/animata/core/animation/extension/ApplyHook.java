package net.quepierts.animata.core.animation.extension;

import net.quepierts.animata.core.animation.animator.Animator;
import org.jetbrains.annotations.NotNull;

public interface ApplyHook<TAnimator extends Animator<?, ?>> extends Extension {
    default void onPreApply(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}

    default void onPostApply(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}
}
