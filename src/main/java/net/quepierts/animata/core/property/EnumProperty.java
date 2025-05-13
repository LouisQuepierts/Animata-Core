package net.quepierts.animata.core.property;

import net.quepierts.animata.core.util.Generic;

import java.util.IdentityHashMap;
import java.util.Map;

public class EnumProperty<T extends Enum<T>> extends AbstractProperty {
    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> ENUMS = new IdentityHashMap<>();

    private final T[] values;
    private int value;

    public EnumProperty(String name, Class<T> pType) {
        super(name);
        this.values = Generic.cast(ENUMS.computeIfAbsent(pType, Class::getEnumConstants));
    }

    @Override
    public void write(float[] pValue) {
        this.value = (int) pValue[0];
    }

    @Override
    public int getDimension() {
        return 1;
    }

    public T enumValue() {
        return this.values[this.value];
    }

    public void setEnumValue(T value) {
        this.value = value.ordinal();
    }

    @Override
    public void fetch(float[] pOut) {
        pOut [0] = this.value;
    }
}
