package net.quepierts.animata.core.util;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

public class SingletonFactory<Key, Value> {
    private final Map<Key, Value> instances;
    private final Value def;

    private boolean freeze = false;

    public SingletonFactory(boolean identity) {
        this(identity, null);
    }

    public SingletonFactory(boolean identity, Value def) {
        this.instances = identity ? new IdentityHashMap<>() : new HashMap<>();
        this.def = def;
    }

    public void register(Key key, Value value) {
        if (this.freeze) {
            throw new RuntimeException("This factory is already frozen");
        }
        this.instances.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <V extends Value> V get(Key key) {
        Value v = this.instances.get(key);
        return (V) (v == null ? this.def : v);
    }

    @SuppressWarnings("unchecked")
    public <K extends Key, V extends Value> V get(K key, Function<K, V> or) {
        Value v = this.instances.get(key);

        if (v == null) {
            v = or.apply(key);
            this.register(key, v);
        }

        return (V) v;
    }

    public void freeze() {
        if (this.freeze) {
            throw new RuntimeException("This factory is already frozen");
        }
        this.freeze = true;
    }
}
