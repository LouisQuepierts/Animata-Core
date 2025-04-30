package net.quepierts.animata.core.animation.cache;

public record RegisterResult(
        AnimationCacheNode node,
        RegisterStatus status,
        String message
) {
    public boolean success() {
        return status == RegisterStatus.SUCCESS;
    }
}
