package net.quepierts.animata.core.animation.binding;

import lombok.AllArgsConstructor;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DirectBinding implements Binding {
    private final Source source;
    private final AnimationCacheNode node;

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
