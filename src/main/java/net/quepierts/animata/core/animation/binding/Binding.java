package net.quepierts.animata.core.animation.binding;

import net.quepierts.animata.core.animation.cache.ValueBuffer;
import org.jetbrains.annotations.NotNull;

public interface Binding {
    void apply(@NotNull ValueBuffer pBuffer, boolean pUpdated);
}
