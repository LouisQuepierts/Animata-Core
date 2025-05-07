package net.quepierts.animata.core.animation.cache.node;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ChildrenContained;

import java.util.Map;

public class NamespaceNode extends AbstractAnimationCacheNode implements ChildrenContained {
    private final Map<String, AnimationCacheNode> children = new Object2ObjectOpenHashMap<>();

    public NamespaceNode(String name) {
        super(name);
    }

    public AnimationCacheNode getChild(String pChildName) {
        return this.children.get(pChildName);
    }

    @Override
    public void addChild(String pName, AnimationCacheNode pNode) {
        this.children.put(pName, pNode);
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
    public void apply(float[] pValue) {

    }

    @Override
    public void fetch(float[] pOut) {

    }
}
