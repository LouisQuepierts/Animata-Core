package net.quepierts.animata.core.animation.binding;

import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * Binding is an interface that binds a value to a buffer.
 */
public interface Binding {
    /**
     * Apply the value from internal buffer to the external buffer.
     * @param pBuffer the internal buffer for fetching value.
     */
    void apply(@NotNull ValueBuffer pBuffer);
}
