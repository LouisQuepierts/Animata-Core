package net.quepierts.animata.core.animation.binding.factories;

import net.quepierts.animata.core.animation.core.IAnimation;
import net.quepierts.animata.core.animation.binding.ConstantSource;
import net.quepierts.animata.core.animation.binding.ISource;
import net.quepierts.animata.core.animation.path.PathNode;

import java.util.Set;

public class EnableSourceFactory implements CascadeSourceFactory {
    private static final Set<String> HANDLED_SUFFIXES = Set.of("enable");
    @Override
    public String getKey() {
        return "enable";
    }

    @Override
    public Set<String> getHandledSuffixes() {
        return HANDLED_SUFFIXES;
    }

    @Override
    public void registerSource(PathNode pNode, String pPrefix, IAnimation pAnimation) {
        ISource source = pAnimation.getSource(pPrefix + ".enable");
        pNode.sources.put("enable", source == null ? new ConstantSource(pPrefix + ".enable", 1.0f) : source);
    }
}
