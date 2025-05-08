package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.AnimationControlBlock;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.runtime.CachedAnimationControlBlock;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleAnimator
        implements Animator<AnimationCache, AnimationSequence> {
    private final Animatable target;
    private final AnimationCache cache;
    private final IAnimataTimeProvider timer;

    private CachedAnimationControlBlock instance;
    private boolean paused;

    public SimpleAnimator(Animatable target, AnimationCache animationCache, IAnimataTimeProvider timer) {
        this.target = target;
        this.cache = animationCache;
        this.timer = timer;
    }

    @Override
    public void update() {
        if (this.paused) {
            return;
        }

        if (!this.isRunning()) {
            return;
        }

        float ticks = this.timer.getCountedTime();
        this.instance.update(ticks);
    }

    @Override
    public void process() {
        if (!this.isRunning()) {
            return;
        }

        this.instance.process();
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

    @Override
    public void pause(@Nullable AnimationCache key) {
        this.paused = true;
    }

    @Override
    public void resume(AnimationCache key) {
        this.paused = false;
    }

    @Override
    public boolean isPaused(AnimationCache key) {
        return false;
    }

    @Override
    public AnimationControlBlock<AnimationCache, AnimationSequence> play(@Nullable AnimationCache pKey, @NotNull AnimationSequence pAnimation) {
        this.instance = new CachedAnimationControlBlock(
                pAnimation,
                this.target,
                this.cache,
                0,
                this.timer.getCountedTime()
        );
        return this.instance;
    }

    @Override
    public void stop(@Nullable AnimationCache pKey) {
        this.instance.release();
        this.instance = null;
    }
}
