package net.quepierts.animata.core.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractProperty implements Property {
    private final String name;
}
