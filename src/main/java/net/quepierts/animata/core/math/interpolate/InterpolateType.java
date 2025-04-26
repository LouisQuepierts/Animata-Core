package net.quepierts.animata.core.math.interpolate;

import lombok.Getter;

// priority: will select the interpolator with higher priority
@Getter
public enum InterpolateType {
    LINEAR(1),
    CATMULL_ROM(2),
    STEP(2);

    private final int priority;

    InterpolateType(int priority) {
        this.priority = priority;
    }
}
