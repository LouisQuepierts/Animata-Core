package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.runtime.RequiredFieldProvider;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.animation.runtime.RequiredField;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AnimationSequence
        extends RequiredFieldProvider {
    default boolean isFinished(RuntimeContext pContext) {
        return pContext.getTime() >= this.getLength().getTick();
    }

    @Override
    default void getRequiredFields(@NotNull Collection<RequiredField> pOut) {

    }

    default void update(@NotNull RuntimeContext pContext) {

    }

    Duration getLength();

    void getAnimationClips(@NotNull Collection<AnimationClip> pOut);

    @Nullable AnimationClip getAnimationClip(@NotNull String name);
}
