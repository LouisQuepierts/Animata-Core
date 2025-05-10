package net.quepierts.animata.core.animation.timeline;

import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.data.Duration;
import net.quepierts.animata.core.math.MathUtils;
import net.quepierts.animata.core.math.interpolate.Interpolator;

public interface Track extends AnimationClip {
    @Override
    default void eval(float[] pBuffer, RuntimeContext pContext) {
        float time = pContext.getProgress();

        int lower = this.getLowerIndex(time);
        int upper = this.getUpperIndex(time);

        VectorKeyFrame last = this.getLowerEntry(time);
        VectorKeyFrame next = this.getUpperEntry(time);

        float delta = MathUtils.saturate((time - last.getTime()) / (next.getTime() - last.getTime()));

        Interpolator interpolator = last.interpolator().select(next.interpolator());
        interpolator.accept(pBuffer, this, lower, upper, delta);
    }

    int getDimension();

    Duration getLength();

    int getLowerIndex(float pTime);

    int getUpperIndex(float pTime);

    VectorKeyFrame getLowerEntry(float pTime);

    VectorKeyFrame getUpperEntry(float pTime);

    VectorKeyFrame get(int pIndex);
}
