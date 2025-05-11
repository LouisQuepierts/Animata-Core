package net.quepierts.animata.core.animation.animator.impl;

import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.animator.base.AbstractExtensibleAnimator;
import net.quepierts.animata.core.animation.animator.base.AnimationControlBlockFactory;
import net.quepierts.animata.core.animation.animator.control.AnimationControlBlock;
import net.quepierts.animata.core.animation.animator.control.CachedAnimationControlBlock;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class CachedSerialAnimator<T> extends AbstractExtensibleAnimator<T, AnimationSequence> {
    public static CachedSerialAnimator<Animatable> simple(
            @NotNull IAnimataTimeProvider pTimeProvider,
            @NotNull Animatable pTarget,
            @NotNull AnimationCache pCache
    ) {
        return new CachedSerialAnimator<>(
                pTimeProvider,
                (pAnimator, pKey, pAnimation) -> new CachedAnimationControlBlock(
                        pAnimation,
                        pAnimator.cache,
                        -1
                ),
                pTarget,
                pCache
        );
    }

    private final AnimationControlBlockFactory<CachedSerialAnimator<T> , T, AnimationSequence> factory;
    private final AnimationCache cache;
    private final T target;

    private AnimationControlBlock<T, AnimationSequence> block;
    private boolean paused;

    public CachedSerialAnimator(
            @NotNull IAnimataTimeProvider pTimeProvider,
            @NotNull AnimationControlBlockFactory<CachedSerialAnimator<T>, T, AnimationSequence> pFactory,
            @NotNull T pTarget,
            @NotNull AnimationCache pCache
    ) {
        super(pTimeProvider, pCache::register);
        this.factory = pFactory;
        this.cache = pCache;
        this.target = pTarget;
    }

    @Override
    protected boolean onUpdate() {
        if (this.paused || !this.isRunning()) {
            return false;
        }

        this.block.update(this.getDeltaTime());

        if (this.block.isFinished()) {
            this.onFinished(this.target);
        }

        return true;
    }

    @Override
    protected boolean onApply() {
        if (!this.isRunning()) {
            return false;
        }

        this.block.apply();
        return true;
    }

    @Override
    protected boolean onProcess() {
        if (this.paused || !this.isRunning()) {
            return false;
        }

        this.block.process();
        return true;
    }

    @Override
    protected AnimationControlBlock<T, AnimationSequence> onPlay(@Nullable T pKey, @NotNull AnimationSequence pAnimation) {
        if (this.block != null) {
            this.block.release();
        }

        this.block = this.factory.create(this, this.target, pAnimation);
        return this.block;
    }

    @Override
    protected void onStop(@Nullable T pKey) {
        if (this.block != null) {
            this.block.release();
            this.block = null;
        }
    }

    @Override
    public boolean isRunning() {
        return this.block != null && this.block.isRunning();
    }

    @Override
    public void pause(@Nullable T key) {
        this.paused = true;
    }

    @Override
    public void resume(T key) {
        this.paused = false;
    }

    @Override
    public boolean isPaused(T key) {
        return this.paused;
    }
}
