package net.quepierts.animata.core.property;

import net.quepierts.animata.core.path.PathResolvable;

public interface Property extends PathResolvable {
    void fetch(float[] pOut);

    void write(float[] pValue);

    @Override
    default Property getChild(String pChildName) {
        return null;
    }

    default int getDimension() {
        return 0;
    }
}
