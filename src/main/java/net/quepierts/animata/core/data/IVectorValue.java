package net.quepierts.animata.core.data;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface IVectorValue {
    IVectorValue cast(DataType pType);

    float get();

    float get(int pIndex);

    void get(float @NotNull[] pOut);

    void get(@NotNull Vector2f pOut);

    void get(@NotNull Vector3f pOut);

    void get(@NotNull Vector4f pOut);

    void get(@NotNull IVectorValue pOut);

    void set(float pValue);

    void set(int pIndex, float pValue);

    void set(float[] pArray);

    void set(Vector2f pVector);

    void set(Vector3f pVector);

    void set(Vector4f pVector);

    void set(IVectorValue pValue);

    int length();
}
