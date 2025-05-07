package net.quepierts.animata.core.property;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FloatProperty extends AbstractProperty {
    private float value;

    public FloatProperty(String name) {
        super(name);
    }

    public FloatProperty(String name, float value) {
        super(name);
        this.value = value;
    }

    @Override
    public void write(float[] pValue) {
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
