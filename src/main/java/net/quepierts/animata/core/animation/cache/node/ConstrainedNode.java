package net.quepierts.animata.core.animation.cache.node;

import lombok.Getter;
import net.quepierts.animata.core.animation.cache.IAnimationCacheNode;
import net.quepierts.animata.core.animation.cache.Toggleable;

public class ConstrainedNode<T extends IAnimationCacheNode> extends AbstractAnimationCacheNode
        implements Toggleable {
    public static final String ENABLED_NAME = "enabled";

    @Getter private final T wrapped;
    private final BooleanNode enabled;

    public ConstrainedNode(String name, T node) {
        super(name);
        this.wrapped = node;
        this.enabled = new BooleanNode(ENABLED_NAME, false);
    }

    @Override
    public IAnimationCacheNode getChild(String pChildName) {
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
}
