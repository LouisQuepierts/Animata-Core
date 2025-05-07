package net.quepierts.animata.core.animation.binding;

import lombok.AllArgsConstructor;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DirectBinding implements Binding {
    private final Property node;
    private final float[] source;

    public DirectBinding(
            @NotNull AnimationClip pAnimationClip,
            @NotNull Property pProperty,
            @NotNull ValueBuffer pBuffer
    ) {
        this.source = pBuffer.get(pAnimationClip);
        this.node = pProperty;
    }

    @Override
    public void apply(@NotNull ValueBuffer pBuffer, boolean pUpdated) {
        if (this.node == null || this.source == null) {
            return;
        }

        this.node.write(this.source);
    }
}
