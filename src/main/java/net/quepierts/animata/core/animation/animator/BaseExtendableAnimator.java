package net.quepierts.animata.core.animation.animator;

import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.animation.handle.AnimationControlBlock;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.animator.extension.AnimationExtensionDispatcher;
import net.quepierts.animata.core.animation.animator.extension.AnimatorExtension;
import net.quepierts.animata.core.animation.handle.AnimationHandle;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public abstract class BaseExtendableAnimator<TKey, TAnimation>
        implements Animator<TKey, TAnimation>, ExtensibleAnimator<TKey, TAnimation> {
    private final AnimationExtensionDispatcher<BaseExtendableAnimator<TKey, TAnimation>, TKey, TAnimation> dispatcher;

    protected final AnimationCache cache;
    protected final IAnimataTimeProvider timer;

    private float lastUpdateTime;
    private float deltaTime;

    protected BaseExtendableAnimator(AnimationCache cache, IAnimataTimeProvider timer) {
        this.cache = cache;
        this.timer = timer;

        this.dispatcher = new AnimationExtensionDispatcher<>(this);
        this.lastUpdateTime = timer.getCountedTime();
    }

    public void registerExtension(AnimatorExtension<BaseExtendableAnimator<TKey, TAnimation>, TKey, TAnimation> extension) {
        this.dispatcher.register(extension, this.cache::register);
    }

    @Override
    public AnimationHandle<TKey, TAnimation> play(
            @Nullable TKey pKey,
            @NotNull TAnimation pAnimation
    ) {
//        this.cache.freezeRegistry();

        float time = this.timer.getCountedTime();
        this.lastUpdateTime = time;
        this.dispatcher.onPlay(pKey, pAnimation, time);
        return this.onPlay(pKey, pAnimation, time);
    }

    @Override
    public void stop(@Nullable TKey pKey) {
        float time = this.timer.getCountedTime();
        this.dispatcher.onStop(pKey, time);
        this.onStop();

        this.cache.reset();
    }

    @Override
    public void update() {
        float time = this.timer.getCountedTime();
        this.deltaTime = time - this.lastUpdateTime;

        this.dispatcher.onPreUpdate(time, this.deltaTime);
        this.onUpdate(time);
        this.dispatcher.onPostUpdate(time, this.deltaTime);
        this.lastUpdateTime = time;
    }

    @Override
    public void process() {
        float time = this.timer.getCountedTime();
        this.dispatcher.onPreProcess(time, this.deltaTime);
        this.onProcess();
        this.dispatcher.onPostProcess(time, this.deltaTime);
    }

    @Override
    public void apply() {
        float time = this.timer.getCountedTime();
        this.dispatcher.onPreApply(time, this.deltaTime);
        this.onApply();
        this.dispatcher.onPostApply(time, this.deltaTime);
    }

    protected abstract AnimationControlBlock<TKey, TAnimation> onPlay(
            @Nullable TKey pKey,
            @NotNull TAnimation pAnimation,
            float pGlobalTime
    );

    protected abstract void onUpdate(float pGlobalTime);

    protected abstract void onApply();

    protected abstract void onProcess();

    protected abstract void onStop();
}
