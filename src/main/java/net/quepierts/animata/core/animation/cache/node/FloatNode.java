package net.quepierts.animata.core.animation.cache.node;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FloatNode extends AbstractAnimationCacheNode {
    private float value;

    public FloatNode(String name) {
        super(name);
    }

    public FloatNode(String name, float value) {
        super(name);
        this.value = value;
    }

    @Override
    public void apply(float[] pValue) {
        this.value = pValue[0];
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void fetch(float[] pOut) {
        pOut[0] = this.value;
    }
}
