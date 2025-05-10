package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.runtime.RequiredField;
import net.quepierts.animata.core.animation.runtime.RequiredFieldProvider;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AnimationSequence
        extends RequiredFieldProvider {
    boolean isFinished(RuntimeContext pContext);

    @Override
    default void getRequiredFields(@NotNull Collection<RequiredField> pOut) {

    }

    default void update(@NotNull RuntimeContext pContext) {
        if (this.isFinished(pContext)) {
            pContext.setProgress(0);
        }
    }

    void getAnimationClips(@NotNull Collection<AnimationClip> pOut);

    @Nullable AnimationClip getAnimationClip(@NotNull String name);
}
