package net.quepierts.animata.core.reference;

import net.quepierts.animata.core.math.interpolate.CatmullRomInterpolator;
import net.quepierts.animata.core.math.interpolate.Interpolator;
import net.quepierts.animata.core.math.interpolate.LinearInterpolator;
import net.quepierts.animata.core.math.interpolate.StepInterpolator;

public class Interpolators {
    public static final Interpolator CATMULLROM = new CatmullRomInterpolator();

    public static final Interpolator STEP = new StepInterpolator();

    public static final Interpolator LINEAR = LinearInterpolator.compatible(LerpFunctions.LINEAR);

    public static final Interpolator SMOOTH = LinearInterpolator.compatible(LerpFunctions.SMOOTH);

    public static final Interpolator S_CURVE = LinearInterpolator.compatible(LerpFunctions.S_CURVE);

    public static final Interpolator BOUNCE = LinearInterpolator.compatible(LerpFunctions.BOUNCE);

    public static final Interpolator NLERP = LinearInterpolator.compatible(LerpFunctions.NLERP);

    public static final Interpolator SLERP = LinearInterpolator.compatible(LerpFunctions.SLERP);
}
