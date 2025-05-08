package net.quepierts.animata.core.animation.runtime;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * provider for required fields
 */
public interface RequiredFieldProvider {
    void getRequiredFields(@NotNull Collection<RequiredField> pOut);
}
