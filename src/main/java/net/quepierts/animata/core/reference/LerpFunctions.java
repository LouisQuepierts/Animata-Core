package net.quepierts.animata.core.reference;

import net.quepierts.animata.core.math.interpolate.LerpFunction;

public class LerpFunctions {
    public static final LerpFunction LINEAR = (last, next, delta) -> last * (1.0F - delta) + next * delta;

    public static final LerpFunction SMOOTH = (last, next, delta) -> {
        delta = 2 * delta - delta * delta;
        return last * (1.0F - delta) + next * delta;
    };

    public static final LerpFunction S_CURVE = (last, next, delta) -> {
        float sqr = delta * delta;
        delta = 3 * sqr - 2 * sqr * delta;
        return last * (1.0F - delta) + next * delta;
    };

    public static final LerpFunction BOUNCE = (last, next, delta) -> {
        delta = 2 * (delta - delta * delta);
        return last * (1.0F - delta) + next * delta;
    };

    public static final LerpFunction NLERP = (last, next, delta) -> last;

    public static final LerpFunction SLERP = (last, next, delta) -> last;
}
