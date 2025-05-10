package net.quepierts.animata.core.math;

public class MathUtils {
    public static final float PI = (float) Math.PI;
    public static final float TAU = (float) (Math.PI * 2);
    public static final float EPSILON = 0.00001f;
    public static final float EPSILON_SQR = EPSILON * EPSILON;

    public static final float DEG2RAD = PI / 180f;
    public static final float RAD2DEG = 180f / PI;

    public static float clamp(float value, float min, float max) {
        if (Float.isNaN(value)) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    public static float saturate(float value) {
        if (Float.isNaN(value)) {
            return 0f;
        }
        return Math.max(0f, Math.min(1f, value));
    }
}
