package net.quepierts.animata.core.animation.binding;

import lombok.Getter;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;

import java.util.Arrays;

public class ConstantAnimationClip implements AnimationClip {
    public static final AnimationClip ZERO = new ConstantAnimationClip("__zero", 0f);
    public static final AnimationClip ONE = new ConstantAnimationClip("__one", 1f);

    @Getter private final String name;
    private final float value;

    public ConstantAnimationClip(String name, float value) {
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
