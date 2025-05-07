package net.quepierts.animata.core.animation.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.animation.cache.Property;

@Getter
@RequiredArgsConstructor
public abstract class AbstractProperty implements Property {
    private final String name;

    @Override
    public Property getChild(String pChildName) {
        return null;
    }
}
