package net.quepierts.animata.core.math.interpolate;

import lombok.Getter;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.animation.timeline.ITrack;

// TODO: Implement for InterpolateStrategies
@Getter
public abstract class Interpolator {
    private final int priority;

    private final InterpolateType type;
    private final DataType dataType;
    private final InterpolateStrategy strategy;

    protected Interpolator(
            InterpolateType pInterpolateType,
            InterpolateStrategy pInterpolateStrategy,
            DataType pDataType
    ) {
        this.priority = pInterpolateType.getPriority();
        this.type = pInterpolateType;
        this.dataType = pDataType;
        this.strategy = pInterpolateStrategy;
    }

    public Interpolator select(Interpolator next) {
        if (this.priority >= next.priority) {
            return this;
        }

        return next;
    }

    public abstract void accept(float[] pBuffer, ITrack pTrack, int pLower, int pUpper, float pDelta);

    public boolean check(DataType pType) {
        return this.strategy.checkType(pType, this.dataType);
    }
}
