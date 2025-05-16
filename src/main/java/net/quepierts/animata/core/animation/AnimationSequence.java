package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.runtime.UniformDeclaration;
import net.quepierts.animata.core.animation.runtime.UniformDeclarationProvider;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AnimationSequence
        extends UniformDeclarationProvider {
    boolean isFinished(RuntimeContext pContext);

    @Override
    default void getUniforms(@NotNull Collection<UniformDeclaration> pOut) {

    }

    default void update(@NotNull RuntimeContext pContext) {
        if (this.isFinished(pContext)) {
            pContext.setProgress(0);
        }
    }

    void getAnimationClips(@NotNull Collection<AnimationClip> pOut);

    @Nullable AnimationClip getAnimationClip(@NotNull String name);
}
