package net.quepierts.animata.core.animation.runtime;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.AnimationControlBlock;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.binding.DirectBinding;
import net.quepierts.animata.core.animation.cache.*;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.animation.binding.Binding;
import net.quepierts.animata.core.property.VectorProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CachedAnimationControlBlock
        implements AnimationControlBlock<AnimationCache, AnimationSequence> {

    private final List<Binding> bindingList = new ArrayList<>();
    private final ValueBuffer buffer = new ValueBuffer();

    private final AnimationSequence animation;
    private final AnimationCache cache;
    private final CachedRuntimeContext context;

    @Getter private final int animationID;
    @Getter private final String domainName;

    private float lastTime;

    private boolean updated = false;

    public CachedAnimationControlBlock(
            @NotNull AnimationSequence pAnimationSequence,
            @NotNull Animatable pTarget,
            @NotNull AnimationCache pCache,
            int animationID,
            float pStartTick
    ) {
        this.animation = pAnimationSequence;
        this.cache = pCache;
        this.animationID = animationID;
        this.lastTime = pStartTick;

        this.domainName = animationID < 1 ? "instance" : String.format("instance%02x", this.animationID);
        this.context = this.init();
    }

    @Override
    public void reset() {
        this.cache.reset();
        this.context.setTime(0);
        this.updated = false;
    }

    @Override
    public void release() {
        this.reset();
        this.buffer.release();
        this.bindingList.clear();
    }

    @Override
    public void update(float pCurrentTime) {
        float delta = pCurrentTime - this.lastTime;
        float time = this.context.getTime();

        if (delta > 0) {
            this.context.setTime(time + delta);
            this.updated = true;
            this.animation.update(this.context);
        }
        // loop for debug
        if (this.animation.isFinished(this.context)) {
            this.context.setTime(0);
        }

        this.lastTime = pCurrentTime;
    }

    public void process() {
        if (!this.updated) {
            return;
        }

        this.buffer.eval(this.context);

        for (Binding binding : this.bindingList) {
            binding.apply(this.buffer);
        }

        this.updated = false;
    }

    public void apply() {
        this.cache.apply();
    }

    @Override
    public float getProgress() {
        return this.context.getTime();
    }

    @Override
    public void setProgress(float progress) {
        this.context.setTime(progress);
    }

    public boolean isRunning() {
        return this.animation != null && !this.animation.isFinished(this.context);
    }

    private CachedRuntimeContext init() {
        this.cache.getTransientDomain(this.domainName);

        CachedRuntimeContext.Builder builder = new CachedRuntimeContext.Builder(this.cache, this.domainName)
                .time(0);

        this.registerRequiredFields(this.animation, builder);

        List<AnimationClip> animationClips = new ObjectArrayList<>();
        this.animation.getAnimationClips(animationClips);

        FieldCaptureContext capturer = new FieldCaptureContext(builder::registered);
        // capture fields
        this.animation.update(capturer);
        this.animation.isFinished(capturer);

        Set<Property> bound = new HashSet<>();

        final float[] dummyBuffer = new float[16];
        for (AnimationClip animationClip : animationClips) {
            // capture fields
            animationClip.eval(dummyBuffer, capturer);

            this.buffer.register(animationClip);
            String name = animationClip.getName();

            Property node = this.cache.getCacheProperty(name);

            if (node != null) {
                if (bound.contains(node)) {
                    log.warn("Node {} is already bound, ignoring source {}.", node.getName(), animationClip.getName());
                    continue;
                }

                if (animationClip.getDimension() != node.getDimension()) {
                    log.warn("Source {} and cache node {} have different dimensions, ignoring cache node.", animationClip.getName(), node.getName());
                    continue;
                }

                bound.add(node);
                this.bindingList.add(new DirectBinding(animationClip, node, this.buffer));
            } else {
                log.warn("Source {} is not bound to any cache node, ignoring source.", animationClip.getName());
            }
        }

        this.registerRequiredFields(capturer, builder);
        return builder.build();
    }

    private void registerRequiredFields(
            @NotNull RequiredFieldProvider pProvider,
            @NotNull CachedRuntimeContext.Builder pBuilder
    ) {
        List<RequiredField> requiredFields = new ObjectArrayList<>();
        pProvider.getRequiredFields(requiredFields);

        for (RequiredField field : requiredFields) {
            if (pBuilder.registered(field.name())) {
                continue;
            }

            Property node = this.cache.getCacheProperty(field.name());
            if (node == null) {
                node = this.cache.getTransientProperty(this.domainName, field.name());
            }

            if (node == null || node.getDimension() < field.dimension()) {
                if (field.type() == RequiredField.Type.READ) {
                    pBuilder.value(field.name(), field.defaultValue());
                } else {
                    VectorProperty vectorProperty = new VectorProperty(field.name(), field.dimension());
                    vectorProperty.write(field.defaultValue());
                    this.cache.addTransientProperty(this.domainName, field.name(), vectorProperty);
                    pBuilder.node(field.name(), vectorProperty);
                }
            } else {
                pBuilder.node(field.name(), node);
            }
        }
    }
}
