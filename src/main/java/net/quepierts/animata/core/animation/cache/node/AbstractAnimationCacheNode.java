package net.quepierts.animata.core.animation.cache.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.animation.cache.IAnimationCacheNode;

@Getter
@RequiredArgsConstructor
public abstract class AbstractAnimationCacheNode implements IAnimationCacheNode {
    private final String name;

    @Override
    public IAnimationCacheNode getChild(String pChildName) {
        return null;
    }
}
