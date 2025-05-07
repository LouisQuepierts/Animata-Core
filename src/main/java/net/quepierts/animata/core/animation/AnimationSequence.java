package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.animation.binding.AnimationClip;
import net.quepierts.animata.core.animation.runtime.RequiredField;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface AnimationSequence {
    default boolean isFinished(float pTime) {
        return pTime >= this.getLength().getTick();
    }

    default void getRequiredFields(List<RequiredField> pOut) {

    }

    default void update(RuntimeContext pContext) {

    }

    Duration getLength();

    void getAnimationClips(Collection<AnimationClip> pOut);

    @Nullable AnimationClip getAnimationClip(String name);
}
