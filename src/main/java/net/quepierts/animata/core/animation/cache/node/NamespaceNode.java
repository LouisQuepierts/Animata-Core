package net.quepierts.animata.core.animation.cache.node;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.cache.ChildrenContained;

import java.util.Map;

@RequiredArgsConstructor
public class NamespaceNode implements ChildrenContained {
    private final Map<String, AnimationCacheNode> children = new Object2ObjectOpenHashMap<>();
    @Getter private final String name;

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
}
