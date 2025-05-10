package net.quepierts.animata.core.animation.skeleton;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.quepierts.animata.core.math.transform.Transform;
import net.quepierts.animata.core.math.transform.Transformable;

import java.util.Map;

@Getter
public final class AnimataBone {
    private static final AnimataBone DUMMY = new AnimataBone(null, new Transform(), "dummy");

    private final Map<String, AnimataBone> subBones = new Object2ObjectOpenHashMap<>();

    private final AnimataBone parent;
    private final Transformable bound;
    private final String name;

    private AnimataBone(AnimataBone parent, Transformable bound, String name) {
        this.parent = parent;
        this.bound = bound;
        this.name = name;
    }

    public static AnimataBone model(AnimataBone parent, Transformable bound, String path) {
        return new AnimataBone(parent, bound, path);
    }

    public static AnimataBone root(Transformable bound) {
        return new AnimataBone(DUMMY, bound, "root");
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public void reset() {
        this.bound.resetPose();
    }
}
