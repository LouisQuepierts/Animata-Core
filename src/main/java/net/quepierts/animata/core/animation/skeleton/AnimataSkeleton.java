package net.quepierts.animata.core.animation.skeleton;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.cache.AnimationCacheProvider;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.math.transform.Transform;
import net.quepierts.animata.core.math.transform.Transformable;
import net.quepierts.animata.core.path.PathResolvable;
import net.quepierts.animata.core.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimataSkeleton
        implements Animatable, AnimationCacheProvider {
    public static final String IDENTIFIER_ROOT = "root";
    public static final String IDENTIFIER_SKELETON = "skeleton";
    public static final AnimataSkeleton DEFAULT = new Builder().build();

    private final AnimataBone root;
    private final ImmutableList<AnimataBone> bones;
    private final ImmutableMap<String, AnimataBone> directMapping;
    private final Object2IntMap<String> ordinalMapping;

    public SkeletonMask mask() {
        return new SkeletonMask(ImmutableSet.copyOf(this.directMapping.keySet()), false);
    }

    public AnimataBone getBone(String bone) {
        return this.directMapping.get(bone);
    }

    public void reset() {
        for (AnimataBone bone : this.bones) {
            bone.reset();
        }
    }

    @Override
    public void getChildren(List<Animatable> pOut) {

    }

    @Override
    public @Nullable PathResolvable getChild(String pChildName) {
        return null;
    }

    public int getChildIndex(String pChildName) {
        return this.ordinalMapping.getOrDefault(pChildName, -1);
    }

    @Override
    public String getName() {
        return IDENTIFIER_SKELETON;
    }

    @Override
    public AnimationCache createAnimationCache() {
        return new AnimataSkeletonCache(this);
    }

    @Override
    public @Nullable Property getProperty(@NotNull String pPath) {
        return null;
    }

    public static class Builder {
        private final Stack<AnimataBone> paths;
        private final Map<AnimataBone, Map<String, AnimataBone>> children;
        private final ImmutableMap.Builder<String, AnimataBone> builder;

        private final AnimataBone root;

        public Builder() {
            this(new Transform());
        }

        public Builder(Transformable root) {
            this.paths = new Stack<>();
            this.children = new HashMap<>();
            this.builder = ImmutableMap.builder();

            AnimataBone bone = AnimataBone.root(root);
            this.builder.put(IDENTIFIER_ROOT, bone);
            this.paths.push(bone);
            this.children.put(bone, bone.getSubBones());

            this.root = bone;
        }

        public Builder bone(String name, Transformable part) {
            if (this.paths.isEmpty()) {
                throw new RuntimeException("Builder missing root");
            }
            
            AnimataBone parent = this.paths.peek();
            AnimataBone bone = AnimataBone.model(parent, part, name);
            this.builder.put(name, bone);
            this.children.get(parent).put(name, bone);
            this.children.put(bone, bone.getSubBones());
            return this;
        }

        public Builder beginGroup(String name, Transformable part) {
            if (this.paths.isEmpty()) {
                throw new RuntimeException("Builder missing root");
            }
            
            AnimataBone parent = this.paths.peek();
            AnimataBone bone = AnimataBone.model(parent, part, name);
            this.paths.push(bone);
            this.builder.put(name, bone);
            this.children.get(parent).put(name, bone);
            this.children.put(bone, bone.getSubBones());
            return this;
        }

        public Builder endGroup() {
            if (this.paths.size() > 1) {
                this.paths.pop();
                return this;
            }

            throw new RuntimeException("Builder missing root");
        }

        public AnimataSkeleton build() {
            ImmutableList.Builder<AnimataBone> list = new ImmutableList.Builder<>();
            it.unimi.dsi.fastutil.PriorityQueue<AnimataBone> queue = new ObjectArrayFIFOQueue<>(this.children.size());
            queue.enqueue(this.root);

            while (!queue.isEmpty()) {
                AnimataBone bone = queue.dequeue();
                list.add(bone);

                for (AnimataBone child : this.children.get(bone).values()) {
                    queue.enqueue(child);
                }
            }

            ImmutableList<AnimataBone> bones = list.build();
            Object2IntMap<String> ordinalMapping = new Object2IntOpenHashMap<>(bones.size());
            for (int i = 0; i < bones.size(); i++) {
                ordinalMapping.put(bones.get(i).getName(), i);
            }

            return new AnimataSkeleton(
                    this.root,
                    bones,
                    this.builder.build(),
                    Object2IntMaps.unmodifiable(ordinalMapping)
            );
        }
    }

    public static class SupplyBuilder {
        @Setter
        @Getter
        private Builder builder;

        @NotNull
        private AnimataSkeleton instance = AnimataSkeleton.DEFAULT;

        @NotNull
        public AnimataSkeleton get() {
            if (this.builder != null) {
                this.instance = this.builder.build();
                this.builder = null;
            }

            return this.instance;
        }

        public boolean isBuilding() {
            return this.builder != null;
        }
    }
}
