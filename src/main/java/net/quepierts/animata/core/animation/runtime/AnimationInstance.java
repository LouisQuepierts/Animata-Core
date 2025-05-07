package net.quepierts.animata.core.animation.runtime;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.binding.AnimationClip;
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
public class AnimationInstance {
    private final List<Binding> bindingList = new ArrayList<>();
    private final ValueBuffer buffer = new ValueBuffer();

    private final AnimationSequence animationSequence;
    private final AnimationCache cache;
    private final RuntimeContext context;

    @Getter private final int animationID;
    @Getter private final String domainName;

    private float lastTime;

    private boolean updated = false;

    public AnimationInstance(
            @NotNull AnimationSequence pAnimationSequence,
            @NotNull Animatable pTarget,
            @NotNull AnimationCache pCache,
            int animationID,
            float pStartTick
    ) {
        this.animationSequence = pAnimationSequence;
        this.cache = pCache;
        this.animationID = animationID;
        this.lastTime = pStartTick;

        this.domainName = animationID < 1 ? "instance" : String.format("instance%02x", this.animationID);
        this.context = this.init();
    }

    public void reset() {
        this.cache.reset();
        this.context.setTime(0);
        this.updated = false;
    }

    public void update(float pCurrentTime) {
        float delta = pCurrentTime - this.lastTime;
        float time = this.context.getTime();

        if (delta > 0) {
            this.context.setTime(time + delta);
            this.updated = true;
            this.animationSequence.update(this.context);
            this.buffer.eval(this.context);
        }
        // loop for debug
        if (this.animationSequence.isFinished(time)) {
            this.context.setTime(0);
        }

        this.lastTime = pCurrentTime;
    }

    public void apply() {
        for (Binding binding : this.bindingList) {
            binding.apply(this.buffer, this.updated);
        }

        this.cache.apply();
        this.updated = false;
    }

    public boolean isRunning() {
        return this.animationSequence != null && !this.animationSequence.isFinished(this.context.getTime());
    }

    private RuntimeContext init() {
        this.cache.getTransientDomain(this.domainName);

        RuntimeContext.Builder builder = new RuntimeContext.Builder(this.cache);

        List<RequiredField> requiredFields = new ObjectArrayList<>();
        this.animationSequence.getRequiredFields(requiredFields);

        for (RequiredField field : requiredFields) {
            Property node = this.cache.getCacheNode(field.name());
            if (node == null) {
                node = this.cache.getTransientNode(this.domainName, field.name());
            }

            if (node == null) {
                if (field.type() == RequiredField.Type.READ) {
                    builder.value(field.name(), field.defaultValue());
                } else {
                    VectorProperty vectorProperty = new VectorProperty(field.name(), field.dimension());
                    vectorProperty.write(field.defaultValue());
                    this.cache.addTransientNode(this.domainName, field.name(), vectorProperty);
                    builder.node(field.name(), vectorProperty);
                }
            } else if (node.getDimension() >= field.dimension()) {
                builder.node(field.name(), node);
            }
        }

        List<AnimationClip> animationClips = new ObjectArrayList<>();
        this.animationSequence.getAnimationClips(animationClips);

        Set<Property> bound = new HashSet<>();

        for (AnimationClip animationClip : animationClips) {
            this.buffer.register(animationClip);
            String name = animationClip.getName();

            Property node = this.cache.getCacheNode(name);

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

        return builder.build();
    }
}
