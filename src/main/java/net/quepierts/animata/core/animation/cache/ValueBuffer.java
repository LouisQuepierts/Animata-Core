package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.binding.AnimationClip;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;

import java.util.IdentityHashMap;
import java.util.Map;

public class ValueBuffer {
    private final Map<AnimationClip, float[]> buffer = new IdentityHashMap<>();

    public void eval(RuntimeContext pContext) {
        for (Map.Entry<AnimationClip, float[]> entry : this.buffer.entrySet()) {
            entry.getKey().eval(entry.getValue(), pContext);
        }
    }

    public void register(AnimationClip pAnimationClip) {
        if (!this.buffer.containsKey(pAnimationClip)) {
            this.buffer.put(pAnimationClip, new float[pAnimationClip.getDimension()]);
        }
    }

    public float[] get(AnimationClip pAnimationClip) {
        return this.buffer.get(pAnimationClip);
    }
}
