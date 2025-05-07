package net.quepierts.animata.core.animation.runtime.field;

public interface RuntimeField {
    void fetch(float[] pOut);

    void write(float[] pIn);

    int getDimension();
}
