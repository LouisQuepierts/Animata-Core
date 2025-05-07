package net.quepierts.animata.core.animation.runtime.field;

import net.quepierts.animata.core.property.Property;

public class CacheNodeField implements RuntimeField {
    private final Property node;

    public CacheNodeField(Property node) {
        this.node = node;
    }

    @Override
    public void fetch(float[] pOut) {
        this.node.fetch(pOut);
    }

    @Override
    public void write(float[] pIn) {
        this.node.write(pIn);
    }

    @Override
    public int getDimension() {
        return this.node.getDimension();
    }
}
