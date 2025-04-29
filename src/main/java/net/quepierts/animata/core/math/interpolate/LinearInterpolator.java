package net.quepierts.animata.core.math.interpolate;

import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.IVectorValue;
import net.quepierts.animata.core.animation.timeline.Track;

public class LinearInterpolator extends Interpolator {
    private final LerpFunction lerpFunction;

    public static LinearInterpolator compatible(LerpFunction pFunction) {
        return new LinearInterpolator(pFunction, InterpolateStrategy.COMPATIBLE, DataType.FLOAT);
    }

    public LinearInterpolator(
            LerpFunction pLerpFunction,
            InterpolateStrategy pInterpolateStrategy,
            DataType pDataType
    ) {
        super(InterpolateType.LINEAR, pInterpolateStrategy, pDataType);
        this.lerpFunction = pLerpFunction;
    }

    @Override
    public void accept(float[] pBuffer, Track pTrack, int pLower, int pUpper, float pDelta) {
        IVectorValue last = pTrack.get(pLower).post();
        IVectorValue next = pTrack.get(pUpper).pre();
        for (int i = 0; i < pTrack.getDimension(); i++) {
            pBuffer[i] = this.lerpFunction.apply(last.get(i), next.get(i), pDelta);
        }
    }
}
