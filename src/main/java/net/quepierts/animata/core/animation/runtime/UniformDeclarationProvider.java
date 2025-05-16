package net.quepierts.animata.core.animation.runtime;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * provider for required fields
 */
public interface UniformDeclarationProvider {
    void getUniforms(@NotNull Collection<UniformDeclaration> pOut);
}
