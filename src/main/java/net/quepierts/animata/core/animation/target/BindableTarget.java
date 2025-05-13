package net.quepierts.animata.core.animation.target;

import net.quepierts.animata.core.path.PathResolvable;
import net.quepierts.animata.core.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BindableTarget extends PathResolvable {
    @Nullable Property getProperty(@NotNull String pPath);

    @Override
    @Nullable
    default PathResolvable getChild(String pChildName) {
        return this.getProperty(pChildName);
    }
}
