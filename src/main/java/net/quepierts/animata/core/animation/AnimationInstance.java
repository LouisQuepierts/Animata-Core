package net.quepierts.animata.core.animation;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.binding.DirectBinding;
import net.quepierts.animata.core.animation.cache.*;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.animation.binding.Binding;
import net.quepierts.animata.core.animation.binding.Source;
import net.quepierts.animata.core.animation.binding.factories.CascadeSourceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AnimationInstance {
    private final List<Binding> bindingList = new ArrayList<>();
    private final ValueBuffer buffer = new ValueBuffer();

    private final Animation animation;
    private final AnimationCache cache;

    private float lastTick;
    private float timer;

    private boolean updated = false;

    public AnimationInstance(
            @NotNull Animation pAnimation,
            @NotNull Animatable pTarget,
            @NotNull AnimationCache pCache,
            @NotNull ImmutableList<CascadeSourceFactory> pFactories,
            float pStartTick
    ) {
        this.animation = pAnimation;
        this.cache = pCache;
        this.lastTick = pStartTick;

        this.init(pFactories);
    }

    public void reset() {

    }

    public void tick(float pCurrentTime) {
        float delta = pCurrentTime - this.lastTick;

        if (delta > 0) {
            this.timer += delta;
            this.updated = true;
            this.buffer.eval(this.timer);
        }
        // loop for debug
        if (this.animation.isFinished(this.timer)) {
            this.timer = 0;
        }

        this.lastTick = pCurrentTime;
    }

    public void apply() {
        for (Binding binding : this.bindingList) {
            binding.apply(this.buffer, this.updated);
        }

        this.cache.apply();
        this.updated = false;
    }

    public boolean isRunning() {
        return this.animation != null && !this.animation.isFinished(this.timer);
    }

    private void init(@NotNull ImmutableList<CascadeSourceFactory> pFactories) {
//        PathNode root = this.buildPathTree(pFactories);
//        this.registerSources(root, pFactories);
//        this.buildBindings(root);

        List<Source> sources = new ObjectArrayList<>();
        this.animation.getSources(sources);

        Set<AnimationCacheNode> bound = new HashSet<>();

        for (Source source : sources) {
            this.buffer.register(source);
            String name = source.getName();

            AnimationCacheNode node = this.cache.getCacheNode(name);

            if (node != null) {
                if (bound.contains(node)) {
                    log.warn("Node {} is already bound, ignoring source {}.", node.getName(), source.getName());
                    continue;
                }

                if (source.getDimension() != node.getDimension()) {
                    log.warn("Source {} and cache node {} have different dimensions, ignoring cache node.", source.getName(), node.getName());
                    continue;
                }

                bound.add(node);
                this.bindingList.add(new DirectBinding(source, node, this.buffer));
            } else {
                log.warn("Source {} is not bound to any cache node, ignoring source.", source.getName());
            }
        }
    }
}
