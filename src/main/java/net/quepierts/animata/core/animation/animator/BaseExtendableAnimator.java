package net.quepierts.animata.core.animation.animator;

import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.Animation;
import net.quepierts.animata.core.animation.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.extension.AnimatorExtension;
import net.quepierts.animata.core.service.AnimataTimeProvider;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public abstract class BaseExtendableAnimator implements Animator {
    private final List<AnimatorExtension> extensions = Lists.newArrayList();

    protected final AnimationCache cache;
    protected final AnimataTimeProvider timer;

    protected BaseExtendableAnimator(AnimationCache cache, AnimataTimeProvider timer) {
        this.cache = cache;
        this.timer = timer;
    }

    public void registerExtension(AnimatorExtension extension) {
        extension.onRegister(this, this.cache::register);
        this.extensions.add(extension);
        this.extensions.sort(AnimatorExtension::compareTo);
    }

    @Override
    public void play(Animation pAnimation) {
        float time = this.timer.getCountedTime();
        for (AnimatorExtension extension : this.extensions) {
            extension.onPlay(this, pAnimation, time);
        }
        this.onPlay(pAnimation, time);
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

    protected abstract void onPlay(Animation pAnimation, float pGlobalTime);

    protected abstract void onUpdate(float pGlobalTime);

    protected abstract void onApply();

    protected abstract void onStop();
}
