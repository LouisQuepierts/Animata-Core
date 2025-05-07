package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.property.Property;

public record RegisterResult(
        Property node,
        RegisterStatus status,
        String message
) {
    public boolean success() {
        return status == RegisterStatus.SUCCESS;
    }
}
