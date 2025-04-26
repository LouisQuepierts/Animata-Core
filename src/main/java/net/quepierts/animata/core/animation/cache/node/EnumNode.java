package net.quepierts.animata.core.animation.cache.node;

import net.quepierts.animata.core.util.Generic;

import java.util.IdentityHashMap;
import java.util.Map;

public class EnumNode<T extends Enum<T>> extends AbstractAnimationCacheNode {
    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> ENUMS = new IdentityHashMap<>();

    private final T[] values;
    private int value;

    public EnumNode(String name, Class<T> pType) {
        super(name);
        this.values = Generic.cast(ENUMS.computeIfAbsent(pType, Class::getEnumConstants));
    }

    @Override
    public void apply(float[] pValue) {
        this.value = (int) pValue[0];
    }

    @Override
    public int getDimension() {
        return 1;
    }

    public T enumValue() {
        return this.values[this.value];
    }
}
