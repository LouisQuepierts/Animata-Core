package net.quepierts.animata.core.animation.property;

import lombok.Getter;
import lombok.Setter;
import net.quepierts.animata.core.animation.cache.Toggleable;

@Setter
@Getter
public class BooleanProperty extends AbstractProperty implements Toggleable {
    private boolean enabled;

    public BooleanProperty(String pName) {
        super(pName);
    }
    
    public BooleanProperty(String pName, boolean pValue) {
        super(pName);
        this.enabled = pValue;
    }
    
    @Override
    public void apply(float[] pValue) {
        this.enabled = pValue[0] != 0.0f;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void fetch(float[] pOut) {
        pOut[0] = this.enabled ? 1.0f : 0.0f;
    }
}
