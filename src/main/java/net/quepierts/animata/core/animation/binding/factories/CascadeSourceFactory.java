package net.quepierts.animata.core.animation.binding.factories;

import net.quepierts.animata.core.animation.core.IAnimation;
import net.quepierts.animata.core.animation.path.PathNode;

import java.util.Set;

public interface CascadeSourceFactory {
    String getKey();

    Set<String> getHandledSuffixes();

    void registerSource(PathNode pNode, String pPrefix, IAnimation pAnimation);
}
