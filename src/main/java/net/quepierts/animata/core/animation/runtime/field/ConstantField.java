package net.quepierts.animata.core.animation.runtime.field;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstantField implements RuntimeField {
    private final float[] value;

    @Override
    public void fetch(float[] pOut) {
        System.arraycopy(this.value, 0, pOut, 0, this.value.length);
    }

    @Override
    public void write(float[] pIn) {

    }

    @Override
    public int getDimension() {
        return this.value.length;
    }
}
