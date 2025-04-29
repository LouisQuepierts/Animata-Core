package net.quepierts.animata.core.animation.binding.factories;

import net.quepierts.animata.core.animation.Animation;
import net.quepierts.animata.core.animation.binding.Source;
import net.quepierts.animata.core.animation.path.PathNode;

import java.util.Collections;
import java.util.Set;

public class ValueSourceFactory implements CascadeSourceFactory {
    @Override
    public String getKey() {
        return "value";
    }

    @Override
    public Set<String> getHandledSuffixes() {
        return Collections.emptySet();
    }

    @Override
    public void registerSource(PathNode pNode, String pPrefix, Animation pAnimation) {
        Source source = pAnimation.getSource(pPrefix);
        if (source != null) {
            pNode.sources.put("value", source);
        }
    }
}
