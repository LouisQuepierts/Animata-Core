package net.quepierts.animata.core.animation.cache;

import net.quepierts.animata.core.property.NamespaceNode;
import net.quepierts.animata.core.property.Property;
import org.jetbrains.annotations.NotNull;

public interface AnimationCache {
    String INPUT_NAMESPACE = "input";
    String RUNTIME = "runtime";

    RegisterResult register(String pName, Property pProperty);

    RegisterResult register(String pParent, String pName, Property pProperty);

    RegisterResult registerNamespaced(String pNamespace, String pName, Property pProperty);

    void reset();

    void apply();

    void freezeRegistry();

    boolean isRegistryFrozen();

    Property getCacheNode(String pPath);

    @NotNull NamespaceNode getTransientDomain(String pName);

    RegisterResult addTransientNode(String pDomain, String pName, Property pProperty);

    Property getTransientNode(String pDomain, String pName);

    void dispose(String pRuntimeDomain);
}
