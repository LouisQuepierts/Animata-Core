package net.quepierts.animata.core.animation.property;

import lombok.Getter;
import net.quepierts.animata.core.animation.cache.Property;
import net.quepierts.animata.core.animation.cache.Toggleable;

public class ConstrainedProperty<T extends Property> extends AbstractProperty
        implements Toggleable {
    public static final String ENABLED_NAME = "enabled";

    @Getter private final T wrapped;
    private final BooleanProperty enabled;

    public ConstrainedProperty(String name, T node) {
        super(name);
        this.wrapped = node;
        this.enabled = new BooleanProperty(ENABLED_NAME, false);
    }

    @Override
    public Property getChild(String pChildName) {
        if (ENABLED_NAME.equals(pChildName)) {
            return this.enabled;
        }
        return this.wrapped.getChild(pChildName);
    }

    @Override
    public void apply(float[] pValue) {
        this.wrapped.apply(pValue);
    }

    @Override
    public int getDimension() {
        return this.wrapped.getDimension();
    }

    public boolean isEnabled() {
        return this.enabled.isEnabled();
    }

    @Override
    public void setEnabled(boolean pEnabled) {
        this.enabled.setEnabled(pEnabled);
    }

    @Override
    public void fetch(float[] pOut) {
        this.wrapped.fetch(pOut);
    }
}
