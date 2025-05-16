package net.quepierts.animata.core.animation.blend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.util.collection.ImmutableFloatTreeMap;

@RequiredArgsConstructor
public class BlendTree1DClip implements AnimationClip {
    private final ImmutableFloatTreeMap<AnimationClip> clips;

    private final float[] buffer;

    private final DataType type;
    @Getter
    private final String name;
    private final String field;

    @Override
    public boolean isFinished(RuntimeContext pContext) {
        return false;
    }

    @Override
    public void eval(float[] pBuffer, RuntimeContext pContext) {
        pContext.fetch(this.field, this.buffer);
        float value = this.buffer[0];

        int lowerIndex = this.clips.getLowerIndex(value);
        int upperIndex = this.clips.getUpperIndex(value);

        if (lowerIndex == upperIndex) {
            this.clips.get(lowerIndex).eval(pBuffer, pContext);
            return;
        }

        float lowerThreshold = this.clips.getKey(lowerIndex);
        float upperThreshold = this.clips.getKey(upperIndex);

        float blend = (value - lowerThreshold) / (upperThreshold - lowerThreshold);
        float invBlend = 1 - blend;

        this.clips.get(lowerIndex).eval(this.buffer, pContext);
        this.clips.get(upperIndex).eval(pBuffer, pContext);

        for (int i = 0; i < pBuffer.length; i++) {
            pBuffer[i] = this.buffer[i] * invBlend + pBuffer[i] * blend;
        }
    }

    @Override
    public int getDimension() {
        return this.type.getLength();
    }
}
