package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.path.PathResolvable;

public interface Property extends PathResolvable, AnimationCacheView {
    void apply(float[] pValue);

    @Override
    Property getChild(String pChildName);

    default int getDimension() {
        return 0;
    }
}
