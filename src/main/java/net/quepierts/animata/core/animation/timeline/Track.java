package net.quepierts.animata.core.animation.timeline;

import net.quepierts.animata.core.animation.binding.Source;
import net.quepierts.animata.core.data.Duration;
import net.quepierts.animata.core.math.interpolate.Interpolator;

public interface Track extends Source {
    @Override
    default void eval(float[] pBuffer, float pTime) {
        int lower = this.getLowerIndex(pTime);
        int upper = this.getUpperIndex(pTime);

        VectorKeyFrame last = this.getLowerEntry(pTime);
        VectorKeyFrame next = this.getUpperEntry(pTime);

        float delta = Math.clamp((pTime - last.getTime()) / (next.getTime() - last.getTime()), 0f, 1f);

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
