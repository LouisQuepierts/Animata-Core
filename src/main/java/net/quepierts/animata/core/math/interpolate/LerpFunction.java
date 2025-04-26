package net.quepierts.animata.core.math.interpolate;

@FunctionalInterface
public interface LerpFunction {
    float apply(float last, float next, float delta);
}
