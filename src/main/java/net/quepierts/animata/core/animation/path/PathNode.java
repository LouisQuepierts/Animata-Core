package net.quepierts.animata.core.animation.path;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.quepierts.animata.core.animation.binding.Source;

import java.util.List;
import java.util.Map;

@Getter
public class PathNode {
    private final String name;
    private final String fullPath;
    private final PathNode parent;

    public final Map<String, List<String>> trackPrefixes;
    public final Map<String, PathNode> children;
    public final Map<String, Source> sources;

    public PathNode(String name, PathNode parent) {
        this.name = name;
        this.parent = parent;

        if (parent != null) {
            this.fullPath = parent.fullPath + "." + name;
        } else {
            this.fullPath = name;
        }

        this.children = new Object2ObjectOpenHashMap<>();
        this.sources = new Object2ObjectOpenHashMap<>(4);
        this.trackPrefixes = new Object2ObjectOpenHashMap<>();
    }

    public Source getSource(String name) {
        return sources.get(name);
    }

    public PathNode getChild(String name) {
        return children.get(name);
    }

    public PathNode getOrCreateChild(String name) {
        PathNode child = this.children.get(name);
        if (child == null) {
            child = new PathNode(name, this);
            this.children.put(name, child);
        }
        return child;
    }
}
