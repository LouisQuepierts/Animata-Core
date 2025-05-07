package net.quepierts.animata.core.animation.runtime.field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class WrappedField implements RuntimeField {
    @Nullable private final Consumer<float[]> getter;
    @Nullable private final Consumer<float[]> setter;
    @Getter private final int dimension;

    @Override
    public void fetch(float[] pOut) {
        if (this.getter != null) {
            this.getter.accept(pOut);
        }
    }

    @Override
    public void write(float[] pIn) {
        if (this.setter != null) {
            this.setter.accept(pIn);
        }
    }
}
