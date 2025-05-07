package net.quepierts.animata.core.animation.runtime.field;

import net.quepierts.animata.core.animation.cache.AnimationCacheNode;

public class CacheNodeField implements RuntimeField {
    private final AnimationCacheNode node;

    public CacheNodeField(AnimationCacheNode node) {
        this.node = node;
    }

    @Override
    public void fetch(float[] pOut) {
        this.node.fetch(pOut);
    }

    @Override
    public void write(float[] pIn) {
        this.node.apply(pIn);
    }

    @Override
    public int getDimension() {
        return this.node.getDimension();
    }
}
