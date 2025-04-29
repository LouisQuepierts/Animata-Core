package net.quepierts.animata.core.animation.cache.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;

@Getter
@RequiredArgsConstructor
public abstract class AbstractAnimationCacheNode implements AnimationCacheNode {
    private final String name;

    @Override
    public AnimationCacheNode getChild(String pChildName) {
        return null;
    }
}
