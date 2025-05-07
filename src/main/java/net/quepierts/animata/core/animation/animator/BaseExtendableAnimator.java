package net.quepierts.animata.core.animation.animator;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.extension.AnimatorExtension;
import net.quepierts.animata.core.service.IAnimataTimeProvider;

import java.util.List;

@Slf4j
public abstract class BaseExtendableAnimator implements Animator {
    private final List<AnimatorExtension> extensions = Lists.newArrayList();

    protected final AnimationCache cache;
    protected final IAnimataTimeProvider timer;

    protected BaseExtendableAnimator(AnimationCache cache, IAnimataTimeProvider timer) {
        this.cache = cache;
        this.timer = timer;
    }

    public void registerExtension(AnimatorExtension extension) {
        extension.onRegister(this, this.cache::register);
        this.extensions.add(extension);
        this.extensions.sort(AnimatorExtension::compareTo);
    }

    @Override
    public void play(AnimationSequence pAnimationSequence) {
        // prevent cache change during working
        this.cache.freezeRegistry();

        float time = this.timer.getCountedTime();
        for (AnimatorExtension extension : this.extensions) {
            extension.onPlay(this, pAnimationSequence, time);
        }
        this.onPlay(pAnimationSequence, time);
    }

    @Override
    public void stop() {
        for (AnimatorExtension extension : this.extensions) {
            extension.onStop(this);
        }
        this.onStop();

        this.cache.reset();
    }

    @Override
    public void update() {
        float time = this.timer.getCountedTime();
        for (AnimatorExtension extension : this.extensions) {
            extension.onPreUpdate(this, time);
        }

        this.onUpdate(time);

        for (AnimatorExtension extension : this.extensions) {
            extension.onPostUpdate(this, time);
        }
    }

    @Override
    public void apply() {
        for (AnimatorExtension extension : this.extensions) {
            extension.onPreApply(this);
        }

        this.onApply();

        for (AnimatorExtension extension : this.extensions) {
            extension.onPostApply(this);
        }
    }

    protected abstract void onPlay(AnimationSequence pAnimationSequence, float pGlobalTime);

    protected abstract void onUpdate(float pGlobalTime);

    protected abstract void onApply();

    protected abstract void onStop();
}
