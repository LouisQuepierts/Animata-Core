package net.quepierts.animata.core.animation.cache.node;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.quepierts.animata.core.animation.cache.IAnimationCacheNode;
import net.quepierts.animata.core.animation.cache.IChildrenContained;

import java.util.Map;

public class NamespaceNode extends AbstractAnimationCacheNode implements IChildrenContained {
    private final Map<String, IAnimationCacheNode> children = new Object2ObjectOpenHashMap<>();

    public NamespaceNode(String name) {
        super(name);
    }

    @Override
    public void apply(float[] pValue) {

    }

    @Override
    public IAnimationCacheNode getChild(String pChildName) {
        return this.children.get(pChildName);
    }

    @Override
    public void addChild(String pName, IAnimationCacheNode pNode) {
        this.children.put(pName, pNode);
    }

    public void print() {
        String prefix = this.getName() + "::";
        for (String string : this.children.keySet()) {
            System.out.println(prefix + string);
        }
    }
}
