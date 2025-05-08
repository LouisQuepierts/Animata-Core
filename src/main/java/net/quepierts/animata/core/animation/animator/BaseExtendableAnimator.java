package net.quepierts.animata.core.animation.animator;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.quepierts.animata.core.animation.AnimationControlBlock;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.extension.AnimatorExtension;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Slf4j
public abstract class BaseExtendableAnimator<K, T>
        implements Animator<K, T> {
    private final List<AnimatorExtension<BaseExtendableAnimator<K, T>, K, T>> extensions = Lists.newArrayList();

    protected final AnimationCache cache;
    protected final IAnimataTimeProvider timer;

    protected BaseExtendableAnimator(AnimationCache cache, IAnimataTimeProvider timer) {
        this.cache = cache;
        this.timer = timer;
    }

    public void registerExtension(AnimatorExtension<BaseExtendableAnimator<K, T>, K, T> extension) {
        extension.onRegister(this, this.cache::register);
        this.extensions.add(extension);
        this.extensions.sort(AnimatorExtension::compareTo);
    }

    @Override
    public AnimationControlBlock<K, T> play(
            @Nullable K pKey,
            @NotNull T pAnimation
    ) {
        this.cache.freezeRegistry();

        float time = this.timer.getCountedTime();
        for (AnimatorExtension<BaseExtendableAnimator<K, T>, K, T> extension : this.extensions) {
            extension.onPlay(this, pAnimation, time);
        }
        return this.onPlay(pKey, pAnimation, time);
    }

    @Override
    public void stop(@Nullable K pKey) {
        for (val extension : this.extensions) {
            extension.onStop(this);
        }
        this.onStop();

        this.cache.reset();
    }

    @Override
    public void update() {
        float time = this.timer.getCountedTime();
        for (val extension : this.extensions) {
            extension.onPreUpdate(this, time);
        }

        this.onUpdate(time);

        for (val extension : this.extensions) {
            extension.onPostUpdate(this, time);
        }
    }

    @Override
    public void process() {

    }

    @Override
    public void apply() {
        for (val extension : this.extensions) {
            extension.onPreApply(this);
        }

        this.onApply();

        for (val extension : this.extensions) {
            extension.onPostApply(this);
        }
    }

    protected abstract AnimationControlBlock<K, T> onPlay(
            @NotNull K pKey,
            @NotNull T pAnimation,
            float pGlobalTime
    );

    protected abstract void onUpdate(float pGlobalTime);

    protected abstract void onApply();

    protected abstract void onProcess();

    protected abstract void onStop();
}
