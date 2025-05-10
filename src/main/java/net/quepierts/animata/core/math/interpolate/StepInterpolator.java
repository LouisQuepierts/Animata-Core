package net.quepierts.animata.core.math.interpolate;

import net.quepierts.animata.core.animation.timeline.Track;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.IVectorValue;

public class StepInterpolator extends Interpolator {
    public StepInterpolator() {
        super(InterpolateType.STEP, InterpolateStrategy.COMPATIBLE, DataType.FLOAT);
    }

    @Override
    public void accept(float[] pBuffer, Track pTrack, int pLower, int pUpper, float pDelta) {
        IVectorValue value = pTrack.get(pLower).post();
        value.get(pBuffer);
    }
}
