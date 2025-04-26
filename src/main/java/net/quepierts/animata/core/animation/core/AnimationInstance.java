package net.quepierts.animata.core.animation.core;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.binding.DirectBinding;
import net.quepierts.animata.core.animation.cache.*;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.animation.target.PreUpdatable;
import net.quepierts.animata.core.animation.binding.CascadeBinding;
import net.quepierts.animata.core.animation.binding.IBinding;
import net.quepierts.animata.core.animation.path.PathNode;
import net.quepierts.animata.core.animation.target.PostUpdatable;
import net.quepierts.animata.core.animation.binding.ISource;
import net.quepierts.animata.core.animation.binding.factories.CascadeSourceFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AnimationInstance {
    private final List<IBinding> bindingList = new ArrayList<>();
    private final ValueBuffer buffer = new ValueBuffer();

    private final IAnimation animation;
    private final IAnimationCache cache;

    @Nullable private final PreUpdatable preUpdatable;
    @Nullable private final PostUpdatable postUpdatable;

    private float startTick;
    private float timer;

    private boolean updated = false;

    public AnimationInstance(
            @NotNull IAnimation pAnimation,
            @NotNull Animatable pTarget,
            @NotNull IAnimationCache pCache,
            @NotNull ImmutableList<CascadeSourceFactory> pFactories,
            float pStartTick
    ) {
        this.animation = pAnimation;
        this.cache = pCache;
        this.startTick = pStartTick;

        this.preUpdatable = pTarget instanceof PreUpdatable ? (PreUpdatable) pTarget : null;
        this.postUpdatable = pTarget instanceof PostUpdatable ? (PostUpdatable) pTarget : null;

        this.init(pFactories);
    }

    public void reset() {

    }

    public void tick(float pCurrentTime) {

        float last = this.timer;
        this.timer = pCurrentTime - this.startTick;

        if (last != this.timer) {
            this.updated = true;
            this.buffer.eval(this.timer);
        }
        // loop for debug
        if (this.animation.isFinished(this.timer)) {
            this.timer = 0;
            this.startTick = pCurrentTime;
        }
    }

    public void apply() {
        for (IBinding binding : this.bindingList) {
            binding.apply(this.buffer, this.updated);
        }

        this.cache.apply();

        if (this.postUpdatable != null) {
            this.postUpdatable.onPostUpdate(this.timer, this.updated);
        }

        this.updated = false;
    }

    public boolean isRunning() {
        return this.animation != null && !this.animation.isFinished(this.timer);
    }

    private void init(@NotNull ImmutableList<CascadeSourceFactory> pFactories) {
//        PathNode root = this.buildPathTree(pFactories);
//        this.registerSources(root, pFactories);
//        this.buildBindings(root);

        List<ISource> sources = new ObjectArrayList<>();
        this.animation.getSources(sources);

        Set<IAnimationCacheNode> bound = new HashSet<>();

        for (ISource source : sources) {
            this.buffer.register(source);
            String name = source.getName();

            IAnimationCacheNode node = this.cache.getCacheNode(name);

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
                this.bindingList.add(new DirectBinding(source, node));
            } else {
                log.warn("Source {} is not bound to any cache node, ignoring source.", source.getName());
            }
        }
    }

    private PathNode buildPathTree(@NotNull ImmutableList<CascadeSourceFactory> pFactories) {
        PathNode root = new PathNode("root", null);

        Set<String> suffixes = new HashSet<>();
        for (CascadeSourceFactory factory : pFactories) {
            suffixes.addAll(factory.getHandledSuffixes());
        }

        List<ISource> sources = new ArrayList<>();
        this.animation.getSources(sources);
        for (ISource source : sources) {
            String fullPath = source.getName();
            String[] parts = fullPath.split("\\.");
            String suffix = null;

            int cut = parts.length;
            String last = parts[parts.length - 1];
            if (suffixes.contains(last)) {
                suffix = last;
                cut = parts.length - 1;
            }

            PathNode node = root;
            for (int i = 1; i < cut; i++) {
                String name = parts[i];
                node = node.getOrCreateChild(name);
            }

            String key = (suffix == null ? "value" : suffix);
            String prefix = String.join(".", Arrays.copyOfRange(parts, 0, cut));
            node.trackPrefixes
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(prefix);
        }
        return root;
    }

    private void registerSources(
            @NotNull PathNode pRoot,
            @NotNull ImmutableList<CascadeSourceFactory> pFactories
    ) {
        Deque<PathNode> stack = new ArrayDeque<>();
        stack.push(pRoot);

        while (!stack.isEmpty()) {
            PathNode node = stack.pop();

            for (CascadeSourceFactory factory : pFactories) {
                List<String> prefixes = node.trackPrefixes.getOrDefault(
                        factory.getKey(),
                        Collections.emptyList()
                );

                if (prefixes.isEmpty() && (
                        "enable".equals(factory.getKey()) || "weight".equals(factory.getKey())
                )) {
                    factory.registerSource(node, node.getFullPath(), this.animation);
                } else {
                    for (String prefix : prefixes) {
                        factory.registerSource(node, prefix, this.animation);
                    }
                }
            }

            node.children.values().forEach(stack::push);
        }
    }

    private void buildBindings(PathNode pNode) {
        ISource main = pNode.sources.get("value");

        if (main != null) {
            List<ISource> enables = new ArrayList<>();
            List<ISource> weights = new ArrayList<>();
            PathNode cursor = pNode;
            while (cursor != null) {
                if (cursor.sources.containsKey("enable")) {
                    enables.add(cursor.sources.get("enable"));
                }
                if (cursor.sources.containsKey("weight")) {
                    weights.add(cursor.sources.get("weight"));
                }
                cursor = cursor.getParent();
            }

            this.buffer.register(main);
            enables.forEach(this.buffer::register);
            weights.forEach(this.buffer::register);

            String path = pNode.getFullPath();
            IAnimationCacheNode node = this.cache.getCacheNode(path);
            this.bindingList.add(new CascadeBinding(main, weights, enables, node));
        }

        for (PathNode child : pNode.children.values()) {
            buildBindings(child);
        }
    }

}
