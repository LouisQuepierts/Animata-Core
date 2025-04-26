package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.path.PathResolvable;

public interface IAnimationCacheNode extends PathResolvable {
    void apply(float[] pValue);

    @Override
    IAnimationCacheNode getChild(String pChildName);

    default int getDimension() {
        return 0;
    }
}
