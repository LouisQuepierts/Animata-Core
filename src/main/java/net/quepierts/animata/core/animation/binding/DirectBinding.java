package net.quepierts.animata.core.animation.binding;

import lombok.AllArgsConstructor;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DirectBinding implements Binding {
    private final AnimationCacheNode node;
    private final float[] source;

    public DirectBinding(
            @NotNull Source pSource,
            @NotNull AnimationCacheNode pNode,
            @NotNull ValueBuffer pBuffer
    ) {
        this.source = pBuffer.get(pSource);
        this.node = pNode;
    }

    @Override
    public void apply(@NotNull ValueBuffer pBuffer, boolean pUpdated) {
        if (this.node == null || this.source == null) {
            return;
        }

        this.node.apply(this.source);
    }
}
