package net.quepierts.animata.core.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class Manager<K, T> {
    private final Map<K, T> byLocation;
    private final Map<T, K> byValue;

    private boolean freeze = false;

    protected Manager() {
        this.byLocation = new Object2ObjectOpenHashMap<>();
        this.byValue = new IdentityHashMap<>();
    }

    public <V extends T> V register(K key, V value) {
        if (this.freeze) {
            throw new RuntimeException("This manager is already frozen");
        }

        this.byLocation.put(key, value);
        if (!this.byValue.containsKey(value)) {
            this.byValue.put(value, key);
        }
        return value;
    }

    public T getValue(K key) {
        return this.byLocation.get(key);
    }

    public K getKey(T value) {
        return this.byValue.get(value);
    }

    public void freeze() {
        this.freeze = true;
    }
}
