package net.quepierts.animata.core.animation.skeleton;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.quepierts.animata.core.animation.cache.*;
import net.quepierts.animata.core.math.transform.Transformable;
import net.quepierts.animata.core.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class AnimataSkeletonCache implements AnimationCache {
    private final GenericAnimationCache delegate;
    private final AnimataSkeleton skeleton;

    private final CacheEntry[] bones;

    public AnimataSkeletonCache(@NotNull AnimataSkeleton pSkeleton) {
        this.delegate = new GenericAnimationCache("skeleton");
        this.skeleton = pSkeleton;

        ImmutableList<AnimataBone> bones = this.skeleton.getBones();
        this.bones = new CacheEntry[bones.size()];

        this.bones[0] = new CacheEntry(this.skeleton.getRoot());

        this.delegate.register(AnimataSkeleton.IDENTIFIER_ROOT, this.bones[0]);

        for (int i = 1; i < bones.size(); i++) {
            AnimataBone bone = bones.get(i);
            this.bones[i] = new CacheEntry(bone);

            assert bone.getParent() != null;
            String parent = bone.getParent().getName();
            this.delegate.register(parent, bone.getName(), this.bones[i]);

            int parentIndex = this.skeleton.getChildIndex(parent);
            this.bones[i].parent = this.bones[parentIndex];
        }
    }

    @Override
    public RegisterResult register(String pName, Property pProperty) {
        return this.delegate.register(pName, pProperty);
    }

    @Override
    public RegisterResult register(String pParent, String pName, Property pProperty) {
        return this.delegate.register(pParent, pName, pProperty);
    }

    @Override
    public RegisterResult registerNamespaced(String pNamespace, String pName, Property pProperty) {
        return this.delegate.registerNamespaced(pNamespace, pName, pProperty);
    }

    @Override
    public void reset() {
        this.delegate.reset();
        for (CacheEntry bone : this.bones) {
            bone.reset();
        }
    }

    @Override
    public void apply() {
        this.delegate.reset();
        for (CacheEntry bone : this.bones) {
            bone.apply();
        }
    }

    @Override
    public void freezeRegistry() {
        this.delegate.freezeRegistry();
    }

    @Override
    public boolean isRegistryFrozen() {
        return this.delegate.isRegistryFrozen();
    }

    @Override
    public Property getCacheNode(String pPath) {
        return this.delegate.getCacheNode(pPath);
    }

    @Override
    public @NotNull NamespaceNode getTransientDomain(String pName) {
        return this.delegate.getTransientDomain(pName);
    }

    @Override
    public void addTransientNode(String pDomain, String pName, Property pProperty) {
        this.delegate.addTransientNode(pDomain, pName, pProperty);
    }

    @Override
    public Property getTransientNode(String pDomain, String pName) {
        return this.delegate.getTransientNode(pDomain, pName);
    }

    @Override
    public void dispose(String pRuntimeDomain) {
        this.delegate.dispose(pRuntimeDomain);
    }

    private static final class CacheEntry
            implements Property, ChildrenContained, Toggleable {

        private final ConstrainedProperty<Vector3fProperty> rotation;
        private final ConstrainedProperty<Vector3fProperty> position;
        private final ConstrainedProperty<Vector3fProperty> scale;
        private final ConstrainedProperty<Vector3fProperty> pivot;

        private final FloatProperty weight;
        private final BooleanProperty enabled;
        private final EnumProperty<SkeletonBlendMode> blendMode;

        private final AnimataBone bone;

        private final Map<String, Property> children = new Object2ObjectOpenHashMap<>();

        private @Nullable CacheEntry parent;

        private CacheEntry(AnimataBone bone) {
            this.bone = bone;
            this.rotation = new ConstrainedProperty<>("rotation", new Vector3fProperty("rotation"));
            this.position = new ConstrainedProperty<>("position", new Vector3fProperty("position"));
            this.scale = new ConstrainedProperty<>("scale", new Vector3fProperty("scale", 1.0f));
            this.pivot = new ConstrainedProperty<>("pivot", new Vector3fProperty("pivot"));
            this.weight = new FloatProperty("weight", 1.0f);
            this.enabled = new BooleanProperty("enabled", false);
            this.blendMode = new EnumProperty<>("blend", SkeletonBlendMode.class);

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

            Transformable bound = this.bone.getBound();
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
        public void write(float[] pValue) {

        }

        @Override
        public Property getChild(String pChildName) {
            return this.children.get(pChildName);
        }

        @Override
        public void addChild(String pName, Property pProperty) {
            this.children.put(pName, pProperty);
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
            this.rotation.write(zero);
            this.position.write(zero);
            this.scale.write(one);

            this.weight.setValue(1.0f);
            this.enabled.setEnabled(false);
        }

        @Override
        public void setEnabled(boolean pEnabled) {
            this.enabled.setEnabled(pEnabled);
        }

        @Override
        public void fetch(float[] pOut) {

        }
    }
}
