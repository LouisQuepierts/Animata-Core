package net.quepierts.animata.core.animation.animator.impl;

import lombok.Getter;
import lombok.Setter;
import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.animator.base.AbstractAnimator;
import net.quepierts.animata.core.animation.animator.control.AnimationControlBlock;
import net.quepierts.animata.core.animation.animator.control.AnimationHandle;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.property.DummyProperty;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class DirectParallelAnimator extends AbstractAnimator<Property, AnimationClip> {
    private final Map<Property, ClipControlBlock> running;

    public DirectParallelAnimator(@NotNull IAnimataTimeProvider pTimeProvider) {
        super(pTimeProvider);

        this.running = new IdentityHashMap<>();
    }

    @Override
    protected boolean onUpdate() {
        for (ClipControlBlock block : this.running.values()) {
            block.update(this.getDeltaTime());
        }
        return false;
    }

    @Override
    public void process() {

    }

    @Override
    public void apply() {

    }

    @Override
    public boolean isRunning() {
        return !this.running.isEmpty();
    }

    @Override
    public void pause(@Nullable Property key) {
        ClipControlBlock block = this.running.get(key);
        if (block != null) {
            block.setPaused(true);
        }
    }

    @Override
    public void resume(Property key) {
        ClipControlBlock block = this.running.get(key);
        if (block != null) {
            block.setPaused(false);
        }
    }

    @Override
    public boolean isPaused(Property key) {
        ClipControlBlock block = this.running.get(key);
        return block != null && block.isPaused();
    }

    @Override
    public AnimationHandle<Property, AnimationClip> play(@Nullable Property pKey, @NotNull AnimationClip pAnimation) {
        Property key = pKey == null ? DummyProperty.INSTANCE : pKey;
        ClipControlBlock block = this.running.get(key);
        if (block == null) {
            block = new ClipControlBlock(key, pAnimation);
            this.running.put(key, block);
        } else {
            block.reset();
            block.clip = pAnimation;
        }

        return block;
    }

    @Override
    public void stop(@Nullable Property pKey) {
        Property key = pKey == null ? DummyProperty.INSTANCE : pKey;
        this.running.remove(key);
    }

    private static class ClipControlBlock
            implements AnimationControlBlock<Property, AnimationClip>, RuntimeContext {

        private final Property property;
        private final float[] buffer;

        private AnimationClip clip;

        @Setter
        @Getter
        private float progress;

        @Setter
        @Getter
        private boolean paused;

        public ClipControlBlock(Property pProperty, AnimationClip pClip) {
            if (pProperty.getDimension() > pClip.getDimension()) {
                throw new RuntimeException("");
            }

            this.buffer = new float[Math.max(pProperty.getDimension(), pClip.getDimension())];
            this.property = pProperty;
            this.clip = pClip;
        }


        @Override
        public void reset() {
            this.progress = 0.0f;
        }

        @Override
        public void release() {

        }

        @Override
        public void update(float pDeltaTime) {
            if (this.paused) {
                return;
            }

            this.progress += pDeltaTime;
            this.clip.eval(this.buffer, this);
            this.property.write(this.buffer);
        }

        @Override
        public boolean isRunning() {
            return this.clip.isFinished(this);
        }

        @Override
        public void fetch(String pPath, float[] pOut) {

        }

        @Override
        public void write(String pPath, float[] pIn) {

        }
    }
}
