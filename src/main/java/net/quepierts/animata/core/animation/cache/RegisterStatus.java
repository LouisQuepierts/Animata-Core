package net.quepierts.animata.core.animation.cache;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RegisterStatus {
    SUCCESS(true, true),
    DUPLICATED_SAME(false, true),
    DUPLICATED_CONFLICT(false, true),
    ILLEGAL_PATH(false, false),
    ILLEGAL_OPERATION(false, false),
    SYSTEM_REGISTERED(false, false);

    private final boolean success;
    private final boolean hasResult;

    public boolean hasResult() {
        return this.hasResult;
    }
}
