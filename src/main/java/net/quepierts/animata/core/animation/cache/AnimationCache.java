package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.animation.target.BindableTarget;
import net.quepierts.animata.core.property.NamespaceNode;
import net.quepierts.animata.core.property.Property;
import org.jetbrains.annotations.NotNull;

public interface AnimationCache extends BindableTarget {
    String INPUT_NAMESPACE = "input";
    String RUNTIME = "runtime";

    RegisterResult register(@NotNull String pName, @NotNull Property pProperty);

    RegisterResult register(@NotNull String pParent, @NotNull String pName, @NotNull Property pProperty);

    RegisterResult registerNamespaced(@NotNull String pNamespace, @NotNull String pName, @NotNull Property pProperty);

    void reset();

    void process();

    void apply();

    void freezeRegistry();

    boolean isRegistryFrozen();

    Property getProperty(@NotNull String pPath);

    @NotNull NamespaceNode getTransientDomain(@NotNull String pName);

    RegisterResult addTransientProperty(@NotNull String pDomain, @NotNull String pName, @NotNull Property pProperty);

    Property getTransientProperty(@NotNull String pDomain, @NotNull String pName);

    void dispose(String pRuntimeDomain);

    @Override
    default String getName() {
        return "cache";
    }
}
