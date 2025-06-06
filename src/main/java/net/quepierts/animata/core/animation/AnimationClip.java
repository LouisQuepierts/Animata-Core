package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.runtime.RuntimeContext;

public interface AnimationClip {
    boolean isFinished(RuntimeContext pContext);

    void eval(float[] pBuffer, RuntimeContext pContext);

    int getDimension();

    String getName();
}
