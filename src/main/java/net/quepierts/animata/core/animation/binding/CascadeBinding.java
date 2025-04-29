package net.quepierts.animata.core.animation.binding;

import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CascadeBinding implements Binding {
    private final Source source;
    private final AnimationCacheNode node;
    private final List<Source> weights;
    private final List<Source> enables;

    public CascadeBinding(
            @NotNull Source pSource,
            @NotNull List<Source> pWeights,
            @NotNull List<Source> pEnables,
            AnimationCacheNode pCacheNode
    ) {
        this.source = pSource;
        this.node = pCacheNode;
        this.weights = pWeights;
        this.enables = pEnables;
    }

    @Override
    public void apply(@NotNull ValueBuffer pBuffer, boolean pUpdated) {
        if (this.node == null) {
            return;
        }

        float[] source = pBuffer.get(this.source);

        if (source == null) {
            return;
        }

        for (Source enable : this.enables) {
            float[] floats = pBuffer.get(enable);
            if (floats != null && floats[0] < 0.5f) {
                return;
            }
        }

        this.node.apply(source);
    }
}
