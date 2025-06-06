package net.quepierts.animata.core.animation.animator.control;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.binding.Binding;
import net.quepierts.animata.core.animation.binding.DirectBinding;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.cache.ValueBuffer;
import net.quepierts.animata.core.animation.runtime.CachedRuntimeContext;
import net.quepierts.animata.core.animation.runtime.FieldCaptureContext;
import net.quepierts.animata.core.animation.runtime.UniformDeclaration;
import net.quepierts.animata.core.animation.runtime.UniformDeclarationProvider;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.property.VectorProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class CachedAnimationControlBlock
        implements AnimationControlBlock {

    private final List<Binding> bindingList = new ArrayList<>();
    private final ValueBuffer buffer = new ValueBuffer();

    private final AnimationSequence animation;
    private final AnimationCache cache;
    private final CachedRuntimeContext context;

    @Getter private final int animationID;
    @Getter private final String domainName;

    private boolean updated = false;

    public CachedAnimationControlBlock(
            @NotNull AnimationSequence pAnimationSequence,
            @NotNull AnimationCache pCache,
            int animationID
    ) {
        this.animation = pAnimationSequence;
        this.cache = pCache;
        this.animationID = animationID;

        this.domainName = animationID < 0 ? "instance" : String.format("instance%02x", this.animationID);
        this.context = this.init();
    }

    @Override
    public void reset() {
        this.cache.reset();
        this.context.setProgress(0);
        this.updated = false;
    }

    @Override
    public void release() {
        this.reset();
        this.buffer.release();
        this.bindingList.clear();
    }

    @Override
    public void update(float pDeltaTime) {
        if (pDeltaTime <= 0) {
            return;
        }

        this.context.increaseTime(pDeltaTime);
        this.updated = true;
        this.animation.update(this.context);
    }

    public void process() {
        if (!this.updated) {
            return;
        }

        this.buffer.eval(this.context);

        for (Binding binding : this.bindingList) {
            binding.apply(this.buffer);
        }
        this.cache.process();

        this.updated = false;
    }

    public void apply() {
        this.cache.apply();
    }

    @Override
    public float getProgress() {
        return this.context.getProgress();
    }

    @Override
    public void setProgress(float progress) {
        this.context.setProgress(progress);
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

            Property node = this.cache.getProperty(name);

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
            @NotNull UniformDeclarationProvider pProvider,
            @NotNull CachedRuntimeContext.Builder pBuilder
    ) {
        List<UniformDeclaration> uniformDeclarations = new ObjectArrayList<>();
        pProvider.getUniforms(uniformDeclarations);

        for (UniformDeclaration field : uniformDeclarations) {
            if (pBuilder.registered(field.name())) {
                continue;
            }

            Property node = this.cache.getProperty(field.name());
            if (node == null) {
                node = this.cache.getTransientProperty(this.domainName, field.name());
            }

            if (node == null || node.getDimension() < field.dimension()) {
                if (field.type() == UniformDeclaration.Type.READ) {
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
