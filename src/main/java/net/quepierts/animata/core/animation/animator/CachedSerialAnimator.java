package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.animator.base.AnimationControlBlockFactory;
import net.quepierts.animata.core.animation.animator.base.BaseAnimator;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.handle.AnimationControlBlock;
import net.quepierts.animata.core.animation.handle.AnimationHandle;
import net.quepierts.animata.core.animation.runtime.CachedAnimationControlBlock;
import net.quepierts.animata.core.animation.target.Animatable;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class CachedSerialAnimator<T> extends BaseAnimator<T, AnimationSequence> {

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
        super(pTimeProvider);
        this.factory = pFactory;
        this.cache = pCache;
        this.target = pTarget;
    }

    @Override
    protected void onUpdate() {
        if (this.paused || !this.isRunning()) {
            return;
        }

        this.block.update(this.getDeltaTime());
    }

    @Override
    public void process() {
        if (this.paused || !this.isRunning()) {
            return;
        }

        this.block.process();
    }

    @Override
    public void apply() {
        if (!this.isRunning()) {
            return;
        }

        this.block.apply();
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

    @Override
    public AnimationHandle<T, AnimationSequence> play(@Nullable T pKey, @NotNull AnimationSequence pAnimation) {
        if (this.block != null) {
            this.block.release();
        }

        this.block = this.factory.create(this, this.target, pAnimation);
        return this.block;
    }

    @Override
    public void stop(@Nullable T pKey) {
        if (this.block != null) {
            this.block.release();
            this.block = null;
        }
    }
}
