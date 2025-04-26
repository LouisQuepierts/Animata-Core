package net.quepierts.animata.core.expression;

import net.quepierts.animata.core.math.interpolate.LerpFunction;

public interface Interpolatable<T> extends Writable<T> {
    void interpolateLerp(LerpFunction function, T p1, T p2, float delta);

    void interpolateCatmullRom(T p0, T p1, T p2, T p3, float delta);

    void setValue(T p);

    boolean isAcceptableTo(Class<?> type);
}
