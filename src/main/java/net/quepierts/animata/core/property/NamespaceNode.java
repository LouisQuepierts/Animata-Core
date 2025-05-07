package net.quepierts.animata.core.property;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class NamespaceNode extends AbstractProperty implements ChildrenContained {
    private final Map<String, Property> children = new Object2ObjectOpenHashMap<>();

    public NamespaceNode(String name) {
        super(name);
    }

    public Property getChild(String pChildName) {
        return this.children.get(pChildName);
    }

    @Override
    public void addChild(String pName, Property pProperty) {
        this.children.put(pName, pProperty);
    }

    public void print() {
        String prefix = this.getName() + "::";
        for (String string : this.children.keySet()) {
            System.out.println(prefix + string);
        }
    }

    public void dispose(String pRuntimeDomain) {
        if (pRuntimeDomain == null || pRuntimeDomain.isEmpty()) {
            this.children.clear();
            return;
        }
        this.children.keySet().removeIf(next -> next.startsWith(pRuntimeDomain));
    }

    @Override
    public void write(float[] pValue) {

    }

    @Override
    public void fetch(float[] pOut) {

    }
}
