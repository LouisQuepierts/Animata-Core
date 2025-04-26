package net.quepierts.animata.core.reference;

public class Interpolations {
    public static float catmullrom(float p0, float p1, float p2, float p3, float delta) {
        float t2 = delta * delta;
        float t3 = t2 * delta;
        return 0.5F * (2.0F * p1 + (-p0 + p2) * delta + (2.0F * p0 - 5.0F * p1 + 4.0F * p2 - p3) * t2 + (-p0 + 3.0F * p1 - 3.0F * p2 + p3) * t3);
    }
}
