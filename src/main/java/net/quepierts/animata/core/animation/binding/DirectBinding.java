package net.quepierts.animata.core.animation.binding;

import lombok.AllArgsConstructor;
import net.quepierts.animata.core.animation.cache.IAnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DirectBinding implements IBinding {
    private final ISource source;
    private final IAnimationCacheNode node;

    @Override
    public void apply(@NotNull ValueBuffer pBuffer, boolean pUpdated) {
        if (this.node == null) {
            return;
        }

        float[] source = pBuffer.get(this.source);
        if (source == null) {
            return;
        }

        this.node.apply(source);
    }
}
