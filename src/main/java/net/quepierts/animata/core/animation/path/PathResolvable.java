package net.quepierts.animata.core.animation.path;

import org.jetbrains.annotations.Nullable;

public interface PathResolvable {
    String getName();

    @Nullable PathResolvable getChild(String pChildName);
}
