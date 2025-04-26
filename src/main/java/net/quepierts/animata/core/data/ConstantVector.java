package net.quepierts.animata.core.data;

import net.quepierts.animata.core.math.MathUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;
import java.util.List;

public final class ConstantVector implements IVectorValue {
    private final float[] values;

    public static ConstantVector single(float value) {
        return new ConstantVector(new float[] {value});
    }

    public static ConstantVector angle(float value) {
        return new ConstantVector(new float[] {value * MathUtils.DEG2RAD});
    }

    public static ConstantVector bool(boolean value) {
        return new ConstantVector(new float[] {value ? 1f : 0f});
    }

    public static ConstantVector vector2(float x, float y) {
        return new ConstantVector(new float[] {x, y});
    }

    public static ConstantVector vector3(float x, float y, float z) {
        return new ConstantVector(new float[] {x, y, z});
    }

    public static ConstantVector rotation(float x, float y, float z) {
        return new ConstantVector(new float[] {x * MathUtils.DEG2RAD, y * MathUtils.DEG2RAD, z * MathUtils.DEG2RAD});
    }

    public static ConstantVector vector4(float x, float y, float z, float w) {
        return new ConstantVector(new float[] {x, y, z, w});
    }

    public static ConstantVector type(DataType pType) {
        return new ConstantVector(new float[pType.getLength()]);
    }

    public static ConstantVector of(float pValue) {
        return new ConstantVector(new float[] {pValue});
    }

    public static ConstantVector of(List<Float> pValues) {
        float[] values = new float[pValues.size()];
        for (int i = 0; i < pValues.size(); i++) {
            values[i] = pValues.get(i);
        }
        return new ConstantVector(values);
    }

    public static ConstantVector of(Vector2f pVector) {
        return new ConstantVector(new float[] {pVector.x, pVector.y});
    }

    public static ConstantVector of(Vector3f pVector) {
        return new ConstantVector(new float[] {pVector.x, pVector.y, pVector.z});
    }

    public static ConstantVector of(Vector4f pVector) {
        return new ConstantVector(new float[] {pVector.x, pVector.y, pVector.z, pVector.w});
    }

    private ConstantVector(float[] values) {
        this.values = values;
    }

    public float get(int index) {
        return this.values[index];
    }

    public float get(int index, float defaultValue) {
        return this.values.length > index ? this.values[index] : defaultValue;
    }

    @Override
    public IVectorValue cast(DataType pType) {
        int length = this.length();
        if (length == pType.getLength()) {
            return this;
        }

        float[] values = new float[pType.getLength()];
        if (length == 1) {
            Arrays.fill(values, this.get());
        } else {
            System.arraycopy(values, 0, this.values, 0, Math.min(values.length, this.values.length));
        }
        return new ConstantVector(values);
    }

    @Override
    public float get() {
        return this.values[0];
    }

    @Override
    public void get(float @NotNull [] array) {
        System.arraycopy(this.values, 0, array, 0, Math.min(this.values.length, array.length));
    }

    public void get(@NotNull Vector2f out) {
        out.set(this.values[0], this.values[1]);
    }

    public void get(@NotNull Vector3f out) {
        out.set(this.values[0], this.values[1], this.values[2]);
    }

    public void get(@NotNull Vector4f out) {
        out.set(this.values[0], this.values[1], this.values[2], this.values[3]);
    }

    @Override
    public void get(@NotNull IVectorValue pOut) {
        pOut.set(this.values);
    }

    // set methods
    @Override
    public void set(float[] array) {
        System.arraycopy(array, 0, this.values, 0, Math.min(array.length, this.values.length));
    }

    @Override
    public void set(float value) {
        this.values[0] = value;
    }

    @Override
    public void set(int index, float value) {
        this.values[index] = value;
    }

    @Override
    public void set(Vector2f vector) {
        this.values[0] = vector.x;
        this.values[1] = vector.y;
    }

    @Override
    public void set(Vector3f vector) {
        this.values[0] = vector.x;
        this.values[1] = vector.y;
        this.values[2] = vector.z;
    }

    @Override
    public void set(Vector4f vector) {
        this.values[0] = vector.x;
        this.values[1] = vector.y;
        this.values[2] = vector.z;
        this.values[3] = vector.w;
    }

    @Override
    public void set(IVectorValue pValue) {
        pValue.get(this.values);
    }

    @Override
    public int length() {
        return this.values.length;
    }
}
