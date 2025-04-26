package net.quepierts.animata.core.animation.timeline;

import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.Duration;
import net.quepierts.animata.core.data.IVectorValue;
import net.quepierts.animata.core.math.interpolate.Interpolator;
import net.quepierts.animata.core.reference.Interpolators;
import net.quepierts.animata.core.util.FloatKeyed;

public record VectorKeyFrame(
        Duration duration,
        Interpolator interpolator,
        IVectorValue pre,
        IVectorValue post
) implements FloatKeyed {
    public static VectorKeyFrame linear(Duration pDuration, IVectorValue pValue) {
        return new VectorKeyFrame(
                pDuration,
                Interpolators.LINEAR,
                pValue, pValue
        );
    }

    public static VectorKeyFrame step(Duration pDuration, IVectorValue pValue) {
        return new VectorKeyFrame(
                pDuration,
                Interpolators.STEP,
                pValue, pValue
        );
    }

    public static VectorKeyFrame cast(VectorKeyFrame pKeyFrame, DataType pType) {
        if (pKeyFrame.pre().length() == pType.getLength()) {
            return pKeyFrame;
        }

        IVectorValue pre = pKeyFrame.pre().cast(pType);

        if (pKeyFrame.pre() == pKeyFrame.post()) {
            return new VectorKeyFrame(pKeyFrame.duration(), pKeyFrame.interpolator(), pre, pre);
        } else {
            return new VectorKeyFrame(pKeyFrame.duration(), pKeyFrame.interpolator(), pre, pKeyFrame.post().cast(pType));
        }
    }

    public float getTime() {
        return this.duration().getTick();
    }

    @Override
    public float getFloatKey() {
        return this.getTime();
    }
}
