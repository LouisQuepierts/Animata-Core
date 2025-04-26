package net.quepierts.animata.core.animation.skeleton;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.quepierts.animata.core.animation.cache.*;
import net.quepierts.animata.core.animation.cache.node.*;
import net.quepierts.animata.core.math.transform.ITransform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class AnimataSkeletonCache implements IAnimationCache {
    private final GenericAnimationCache cache;
    private final AnimataSkeleton skeleton;

    private final CacheEntry[] bones;

    public AnimataSkeletonCache(@NotNull AnimataSkeleton pSkeleton) {
        this.cache = new GenericAnimationCache("skeleton");
        this.skeleton = pSkeleton;

        ImmutableList<AnimataBone> bones = this.skeleton.getBones();
        this.bones = new CacheEntry[bones.size()];

        this.bones[0] = new CacheEntry(this.skeleton.getRoot());

        this.cache.register(AnimataSkeleton.IDENTIFIER_ROOT, this.bones[0]);

        for (int i = 1; i < bones.size(); i++) {
            AnimataBone bone = bones.get(i);
            this.bones[i] = new CacheEntry(bone);

            assert bone.getParent() != null;
            String parent = bone.getParent().getName();
            this.cache.register(parent, bone.getName(), this.bones[i]);

            int parentIndex = this.skeleton.getChildIndex(parent);
            this.bones[i].parent = this.bones[parentIndex];
        }
    }

    @Override
    public void register(String pName, IAnimationCacheNode pNode) {
        this.cache.register(pName, pNode);
    }

    @Override
    public void register(String pParent, String pName, IAnimationCacheNode pNode) {
        this.cache.register(pParent, pName, pNode);
    }

    @Override
    public void registerNamespaced(String pNamespace, String pName, IAnimationCacheNode pNode) {
        this.cache.registerNamespaced(pNamespace, pName, pNode);
    }

    @Override
    public void reset() {
        for (CacheEntry bone : this.bones) {
            bone.reset();
        }
    }

    @Override
    public void apply() {
        for (CacheEntry bone : this.bones) {
            bone.apply();
        }
    }

    @Override
    public IAnimationCacheNode getCacheNode(String pPath) {
        return this.cache.getCacheNode(pPath);
    }

    private static final class CacheEntry
            implements IAnimationCacheNode, IChildrenContained, Toggleable {

        private final ConstrainedNode<Vector3fNode> rotation;
        private final ConstrainedNode<Vector3fNode> position;
        private final ConstrainedNode<Vector3fNode> scale;
        private final ConstrainedNode<Vector3fNode> pivot;

        private final FloatNode weight;
        private final BooleanNode enabled;
        private final EnumNode<SkeletonBlendMode> blendMode;

        private final AnimataBone bone;

        private final Map<String, IAnimationCacheNode> children = new Object2ObjectOpenHashMap<>();

        private @Nullable CacheEntry parent;

        private CacheEntry(AnimataBone bone) {
            this.bone = bone;
            this.rotation = new ConstrainedNode<>("rotation", new Vector3fNode("rotation"));
            this.position = new ConstrainedNode<>("position", new Vector3fNode("position"));
            this.scale = new ConstrainedNode<>("scale", new Vector3fNode("scale", 1.0f));
            this.pivot = new ConstrainedNode<>("pivot", new Vector3fNode("pivot"));
            this.weight = new FloatNode("weight", 1.0f);
            this.enabled = new BooleanNode("enabled", false);
            this.blendMode = new EnumNode<>("blend", SkeletonBlendMode.class);

            this.children.put("rotation", this.rotation);
            this.children.put("position", this.position);
            this.children.put("scale", this.scale);
            this.children.put("pivot", this.pivot);
            this.children.put("enabled", this.enabled);
            this.children.put("weight", this.weight);
            this.children.put("blend", this.blendMode);
        }

        public void apply() {
            if (!this.enabled.isEnabled()) {
                return;
            }

            ITransform bound = this.bone.getBound();
            Vector3f rotation = bound.getRotation();

            if (this.rotation.isEnabled()) {
                this.blend(rotation, this.rotation.getWrapped().getCache());
                bound.setRotation(rotation);
            }

            boolean positionEnabled = this.position.isEnabled();
            boolean pivotEnabled = this.pivot.isEnabled();
            if (positionEnabled || pivotEnabled) {
                Vector3f position = bound.getPosition();

                if (pivotEnabled) {
                    Quaternionf rot = new Quaternionf().rotateZYX(rotation.z, rotation.y, rotation.x);
                    Vector3f pivot = this.pivot.getWrapped().getCache();
                    position.add(pivot)
                            .rotate(rot)
                            .sub(pivot);
                }

                if (positionEnabled) {
                    this.blend(position, this.position.getWrapped().getCache());
                }

                bound.setPosition(position);
            }

            if (this.scale.isEnabled()) {
                Vector3f scale = bound.getScale();
                this.blend(scale, this.scale.getWrapped().getCache());
                bound.setScale(scale);
            }
        }

        @Override
        public String getName() {
            return this.bone.getName();
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

        private void blend(
                Vector3f pVector,
                Vector3f pBlend
        ) {
            float weight = this.weight.getValue();
            switch (this.blendMode.enumValue()) {
                case OVERRIDE:
                    pVector.set(pBlend);
                    break;
                case MIX:
                    pVector.mul(1.0f - weight)
                            .add(
                                    pBlend.x * weight,
                                    pBlend.y * weight,
                                    pBlend.z * weight
                            );
                    break;
                case ADD:
                    pVector.add(
                            pBlend.x * weight,
                            pBlend.y * weight,
                            pBlend.z * weight
                            );
                    break;
            }
        }

        private void blend(
                @NotNull Vector3f pSource,
                float @NotNull [] pBlend,
                Vector3f pTarget
        ) {
            float weight = this.weight.getValue();
            switch (this.blendMode.enumValue()) {
                case OVERRIDE:
                    pTarget.set(pSource);
                    break;
                case MIX:
                    pTarget.set(
                            pBlend[0] * weight + pSource.x * (1.0f - weight),
                            pBlend[1] * weight + pSource.y * (1.0f - weight),
                            pBlend[2] * weight + pSource.z * (1.0f - weight)
                    );
                    break;
                case ADD:
                    pTarget.set(
                            pBlend[0] * weight + pSource.x,
                            pBlend[1] * weight + pSource.y,
                            pBlend[2] * weight + pSource.z
                    );
                    break;
            }
        }

        public void reset() {
            float[] one = new float[]{1.0f, 1.0f, 1.0f};
            float[] zero = new float[]{0.0f, 0.0f, 0.0f};
            this.rotation.apply(zero);
            this.position.apply(zero);
            this.scale.apply(one);

            this.weight.setValue(1.0f);
            this.enabled.setEnabled(false);
        }

        @Override
        public void setEnabled(boolean pEnabled) {
            this.enabled.setEnabled(pEnabled);
        }
    }
}
