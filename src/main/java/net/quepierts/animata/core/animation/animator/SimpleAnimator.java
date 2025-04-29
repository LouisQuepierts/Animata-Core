package net.quepierts.animata.core.animation.animator;

import com.google.common.collect.ImmutableList;
import net.quepierts.animata.client.ClientTickHandler;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.AnimationInstance;
import net.quepierts.animata.core.animation.Animation;
import net.quepierts.animata.core.animation.Animator;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.animation.binding.factories.CascadeSourceFactory;
import net.quepierts.animata.core.animation.binding.factories.EnableSourceFactory;
import net.quepierts.animata.core.animation.binding.factories.ValueSourceFactory;

public class SimpleAnimator implements Animator {
    private final Animatable target;
    private final AnimationCache cache;
    private final ImmutableList<CascadeSourceFactory> factories;

    private AnimationInstance instance;

    public SimpleAnimator(Animatable target, AnimationCache animationCache) {
        this.target = target;
        this.cache = animationCache;

        this.factories = ImmutableList.of(
                new ValueSourceFactory(),
                new EnableSourceFactory()
        );
    }

    @Override
    public void play(Animation pAnimation) {
        this.instance = new AnimationInstance(
                pAnimation,
                this.target,
                this.cache,
                this.factories,
                ClientTickHandler.IMPLEMENT.getGameTimeTick()
        );
    }

    @Override
    public void stop() {
        this.instance.reset();
        this.instance = null;
    }

    @Override
    public void terminate() {
        this.instance.reset();
        this.instance = null;
    }

    @Override
    public void update() {
        if (!this.isRunning()) {
            return;
        }

        float ticks = ClientTickHandler.IMPLEMENT.getGameTimeTick();
        this.instance.tick(ticks);
    }

    @Override
    public void apply() {
        if (!this.isRunning()) {
            return;
        }

        this.instance.apply();
    }

    @Override
    public boolean isRunning() {
        return this.instance != null && this.instance.isRunning();
    }
}
