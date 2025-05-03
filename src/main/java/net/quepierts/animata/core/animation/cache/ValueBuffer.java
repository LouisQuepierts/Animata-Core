package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.binding.Source;

import java.util.IdentityHashMap;
import java.util.Map;

public class ValueBuffer {
    private final Map<Source, float[]> buffer = new IdentityHashMap<>();

    public void eval(float pTime) {
        for (Map.Entry<Source, float[]> entry : this.buffer.entrySet()) {
            entry.getKey().eval(entry.getValue(), pTime);
        }
    }

    public void register(Source pSource) {
        if (!this.buffer.containsKey(pSource)) {
            this.buffer.put(pSource, new float[pSource.getDimension()]);
        }
    }

    public float[] get(Source pSource) {
        return this.buffer.get(pSource);
    }
}
