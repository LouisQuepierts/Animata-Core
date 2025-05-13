package net.quepierts.animata.core.animation.blend;

import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.runtime.RequiredField;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BlendTreeAnimationSequence implements AnimationSequence {
    @Override
    public boolean isFinished(RuntimeContext pContext) {
        return false;
    }

    @Override
    public void getRequiredFields(@NotNull Collection<RequiredField> pOut) {
        pOut.add(new RequiredField("blend_tree", 3, new float[] {0, 0, 0}, RequiredField.Type.READWRITE));
    }

    @Override
    public void update(@NotNull RuntimeContext pContext) {
        AnimationSequence.super.update(pContext);
    }

    @Override
    public void getAnimationClips(@NotNull Collection<AnimationClip> pOut) {

    }

    @Override
    public @Nullable AnimationClip getAnimationClip(@NotNull String name) {
        return null;
    }
}
