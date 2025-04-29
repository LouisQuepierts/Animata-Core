package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.path.PathResolvable;

public interface AnimationCacheNode extends PathResolvable {
    void apply(float[] pValue);

    @Override
    AnimationCacheNode getChild(String pChildName);

    default int getDimension() {
        return 0;
    }
}
