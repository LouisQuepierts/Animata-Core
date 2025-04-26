package net.quepierts.animata.core.math.interpolate;

import net.quepierts.animata.core.data.DataType;

import java.util.function.BiPredicate;

public enum InterpolateStrategy {
    COMPATIBLE((a, b) -> b.getLength() <= a.getLength() && a.getLength() % b.getLength() == 0),
    MATCHES((a, b) -> a.getLength() == b.getLength());

    private final BiPredicate<DataType, DataType> predicate;

    InterpolateStrategy(BiPredicate<DataType, DataType> predicate) {
        this.predicate = predicate;
    }

    public static InterpolateStrategy of(String name) {
        return switch (name) {
            case "compatible" -> COMPATIBLE;
            case "matches" -> MATCHES;
            default -> throw new IllegalArgumentException("Unknown InterpolateStrategy: " + name);
        };
    }

    public boolean checkType(DataType pInput, DataType pInterpolator) {
        return this.predicate.test(pInput, pInterpolator);
    }
}
