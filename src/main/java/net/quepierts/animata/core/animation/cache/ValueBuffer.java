package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.binding.ISource;

import java.util.IdentityHashMap;
import java.util.Map;

public class ValueBuffer {
    private final Map<ISource, float[]> buffer = new IdentityHashMap<>();
    private final Map<ISource, float[]> pool = new IdentityHashMap<>();

    public void eval(float pTime) {
        for (Map.Entry<ISource, float[]> entry : this.pool.entrySet()) {
            entry.getKey().eval(entry.getValue(), pTime);
            this.buffer.put(entry.getKey(), entry.getValue());
        }
    }

    public void register(ISource pSource) {
        if (!this.pool.containsKey(pSource)) {
            this.pool.put(pSource, new float[pSource.getDimension()]);
        }
    }

    public float[] get(ISource pSource) {
        return this.buffer.get(pSource);
    }
}
