package net.quepierts.animata.core.util.collection;

import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import lombok.Getter;
import net.quepierts.animata.core.util.FloatKeyed;
import net.quepierts.animata.core.util.Generic;

import java.util.Arrays;

public class ImmutableFloatTreeMap<T> implements IFloatTreeMap<T> {
    private final float[] keys;
    private final T[] values;
    private final int max;

    @Getter private final int size;

    public static <T extends FloatKeyed> ImmutableFloatTreeMap<T> of(T[] values) {
        Arrays.sort(values);
        float[] keys = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            keys[i] = values[i].getFloatKey();
        }
        return new ImmutableFloatTreeMap<>(keys, values);
    }

    public static <T> ImmutableFloatTreeMap<T> of(float[] sortedKeys, Float2ObjectMap<T> map) {
        T[] values = Generic.newArray(map.size());
        for (int i = 0; i < map.size(); i++) {
            values[i] = map.get(sortedKeys[i]);
        }
        return new ImmutableFloatTreeMap<>(sortedKeys, values);
    }

    private ImmutableFloatTreeMap(float[] keys, T[] values) {
        this.keys = keys;
        this.values = values;
        this.max = keys.length - 1;
        this.size = keys.length;
    }

    public int getLowerIndex(float pKey) {
        int index = Arrays.binarySearch(this.keys, pKey);

        if (index >= 0) {
            return Math.clamp(index - 1, 0, max);
        }

        return Math.clamp(-index - 2, 0, max);
    }

    public int getUpperIndex(float pKey) {
        int index = Arrays.binarySearch(this.keys, pKey);

        if (index >= 0) {
            return Math.clamp(index, 0, max);
        }

        return Math.clamp(-index - 1, 0, max);
    }

    public T getLowerEntry(float pKey) {
        int index = getLowerIndex(pKey);

        return this.values[index];
    }

    public T getUpperEntry(float pKey) {
        int index = getUpperIndex(pKey);

        return this.values[index];
    }

    @Override
    public void insert(float pKey, T pValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSpecific(float pKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRange(float pMin, float pMax) {
        throw new UnsupportedOperationException();
    }

    public T first() {
        return this.values[0];
    }

    public T last() {
        return this.values[this.max];
    }

    @Override
    public T get(int pIndex) {
        return this.values[pIndex];
    }
}
