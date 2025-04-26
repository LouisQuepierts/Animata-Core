package net.quepierts.animata.core.animation.binding;

import net.quepierts.animata.core.animation.cache.IAnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CascadeBinding implements IBinding {
    private final ISource source;
    private final IAnimationCacheNode node;
    private final List<ISource> weights;
    private final List<ISource> enables;

    public CascadeBinding(
            @NotNull ISource pSource,
            @NotNull List<ISource> pWeights,
            @NotNull List<ISource> pEnables,
            IAnimationCacheNode pCacheNode
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

        for (ISource enable : this.enables) {
            float[] floats = pBuffer.get(enable);
            if (floats != null && floats[0] < 0.5f) {
                return;
            }
        }

        this.node.apply(source);
    }
}
