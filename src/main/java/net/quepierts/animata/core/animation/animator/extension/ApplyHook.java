package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.base.Animator;
import net.quepierts.animata.core.animation.animator.base.ExtensibleAnimator;
import org.jetbrains.annotations.NotNull;

public interface ApplyHook<TAnimator extends ExtensibleAnimator<?, ?>> extends Extension {
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
