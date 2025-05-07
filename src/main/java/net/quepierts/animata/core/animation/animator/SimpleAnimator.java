package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.Animator;
import net.quepierts.animata.core.animation.runtime.AnimationInstance;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.service.IAnimataTimeProvider;

public class SimpleAnimator implements Animator {
    private final Animatable target;
    private final AnimationCache cache;
    private final IAnimataTimeProvider timer;

    private AnimationInstance instance;

    public SimpleAnimator(Animatable target, AnimationCache animationCache, IAnimataTimeProvider timer) {
        this.target = target;
        this.cache = animationCache;
        this.timer = timer;

    }

    @Override
    public void play(AnimationSequence pAnimationSequence) {
        this.instance = new AnimationInstance(
                pAnimationSequence,
                this.target,
                this.cache,
                0,
                this.timer.getCountedTime()
        );
    }

    @Override
    public void stop() {
        this.instance.reset();
        this.instance = null;
    }

    @Override
    public void update() {
        if (!this.isRunning()) {
            return;
        }

        float ticks = this.timer.getCountedTime();
        this.instance.update(ticks);
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
