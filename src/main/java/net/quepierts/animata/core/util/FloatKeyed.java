package net.quepierts.animata.core.util;

public interface FloatKeyed extends Comparable<FloatKeyed> {
    float getFloatKey();

    default int compareTo(FloatKeyed o) {
        return Float.compare(this.getFloatKey(), o.getFloatKey());
    }
}
