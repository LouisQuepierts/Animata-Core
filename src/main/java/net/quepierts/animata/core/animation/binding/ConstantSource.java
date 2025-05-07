package net.quepierts.animata.core.animation.binding;

import lombok.Getter;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;

import java.util.Arrays;

public class ConstantSource implements Source {
    public static final Source ZERO = new ConstantSource("__zero", 0f);
    public static final Source ONE = new ConstantSource("__one", 1f);

    @Getter private final String name;
    private final float value;

    public ConstantSource(String name, float value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void eval(float[] pBuffer, RuntimeContext pContext) {
        Arrays.fill(pBuffer, this.value);
    }

    @Override
    public int getDimension() {
        return 1;
    }
}
