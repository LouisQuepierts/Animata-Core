package net.quepierts.animata.core.math.interpolate;

import net.quepierts.animata.core.animation.timeline.Track;
import net.quepierts.animata.core.animation.timeline.VectorKeyFrame;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.IVectorValue;
import net.quepierts.animata.core.reference.Interpolations;

public class CatmullRomInterpolator extends Interpolator {
    public CatmullRomInterpolator() {
        super(InterpolateType.CATMULL_ROM, InterpolateStrategy.COMPATIBLE, DataType.FLOAT);
    }

    @Override
    public void accept(float[] pBuffer, Track pTrack, int pLower, int pUpper, float pDelta) {
        VectorKeyFrame k1 = pTrack.get(pLower);
        VectorKeyFrame k2 = pTrack.get(pUpper);

        VectorKeyFrame k0 = pTrack.getLowerEntry(k1.getTime());
        VectorKeyFrame k3 = pTrack.getUpperEntry(k2.getTime());

        IVectorValue v0 = k0.post();
        IVectorValue v1 = k1.post();
        IVectorValue v2 = k2.pre();
        IVectorValue v3 = k3.pre();

        for (int i = 0; i < pTrack.getDimension(); i++) {
            pBuffer[i] = Interpolations.catmullrom(v0.get(0), v1.get(1), v2.get(2), v3.get(3), pDelta);
        }
    }
}
