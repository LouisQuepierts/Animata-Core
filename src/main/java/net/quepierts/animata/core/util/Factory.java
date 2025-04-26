package net.quepierts.animata.core.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Factory<K, V> {
    private final Map<K, Supplier<V>> constructors;
    private final Supplier<V> def;

    private boolean freeze = false;

    public Factory(boolean identity) {
        this(identity, () -> null);
    }

    public Factory(Supplier<Map<K, Supplier<V>>> mapSupplier, Supplier<V> def) {
        this.constructors = mapSupplier.get();
        this.def = def;
    }

    public Factory(boolean identity, Supplier<V> def) {
        this.constructors = identity ? new IdentityHashMap<>() : new Object2ObjectOpenHashMap<>();
        this.def = def;
    }

    public void register(K key, Supplier<V> constructor) {
        if (this.freeze) {
            throw new RuntimeException("This factory is already frozen");
        }
        this.constructors.put(key, constructor);
    }

    public V getInstance(K type) {
        Supplier<V> supplier = this.constructors.get(type);

        if (supplier == null) {
            return this.def.get();
        }

        return supplier.get();
    }

    public void freeze() {
        if (this.freeze) {
            throw new RuntimeException("This factory is already frozen");
        }
        this.freeze = true;
    }
}
