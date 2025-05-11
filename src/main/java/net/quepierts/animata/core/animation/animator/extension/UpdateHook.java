package net.quepierts.animata.core.animation.animator.extension;

import net.quepierts.animata.core.animation.animator.base.ExtensibleAnimator;
import org.jetbrains.annotations.NotNull;

public interface UpdateHook<TAnimator extends ExtensibleAnimator<?, ?>> extends Extension {
    default void onPreUpdate(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}

    default void onPostUpdate(
            @NotNull TAnimator pAnimator,
            float pGlobalTime,
            float pDeltaTime
    ) {}
}
