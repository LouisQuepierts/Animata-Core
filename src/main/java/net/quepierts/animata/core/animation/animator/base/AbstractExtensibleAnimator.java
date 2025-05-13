package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.animator.control.AnimationControlBlock;
import net.quepierts.animata.core.animation.animator.control.AnimationHandle;
import net.quepierts.animata.core.animation.animator.extension.AnimationExtensionDispatcher;
import net.quepierts.animata.core.animation.animator.extension.AnimatorExtension;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExtensibleAnimator<TTarget, TAnimation>
        extends AbstractAnimator<TTarget, TAnimation>
        implements ExtensibleAnimator<TTarget, TAnimation> {

    private final AnimationExtensionDispatcher<TTarget, TAnimation> dispatcher;
    private final AnimationCacheRegistrar registrar;

    public AbstractExtensibleAnimator(
            @NotNull IAnimataTimeProvider pTimeProvider,
            @NotNull AnimationCacheRegistrar pRegistrar
    ) {
        super(pTimeProvider);
        this.registrar = pRegistrar;
        this.dispatcher = new AnimationExtensionDispatcher<>(this);
    }

    protected abstract boolean onApply();

    protected abstract boolean onProcess();

    protected abstract AnimationControlBlock onPlay(
            @Nullable TTarget pKey,
            @NotNull TAnimation pAnimation
    );

    protected abstract void onStop(@Nullable TTarget pKey);

    @Override
    public void addExtension(AnimatorExtension<TTarget, TAnimation> pExtension) {
        this.dispatcher.register(pExtension, this.registrar);
    }

    @Override
    public void update() {
        float time = this.timeProvider.getCountedTime();
        this.deltaTime = time - this.lastUpdatedTime;

        this.dispatcher.onPreUpdate(time, this.deltaTime);
        if (!this.onUpdate()) {
            this.dispatcher.onPostUpdate(time, this.deltaTime);
        }

        this.lastUpdatedTime = time;
    }

    @Override
    public void process() {
        float time = this.timeProvider.getCountedTime();

        this.dispatcher.onPreProcess(time, this.deltaTime);
        if (this.onProcess()) {
            this.dispatcher.onPostProcess(time, this.deltaTime);
        }
    }

    @Override
    public void apply() {
        float time = this.timeProvider.getCountedTime();

        this.dispatcher.onPreApply(time, this.deltaTime);
        if (this.onApply()) {
            this.dispatcher.onPostApply(time, this.deltaTime);
        }
    }

    @Override
    public AnimationHandle play(@Nullable TTarget pKey, @NotNull TAnimation pAnimation) {
        this.dispatcher.onPlay(pKey, pAnimation);
        return this.onPlay(pKey, pAnimation);
    }

    @Override
    public void stop(@Nullable TTarget pKey) {
        this.dispatcher.onStop(pKey);
        this.onStop(pKey);
    }

    protected final void onFinished(@Nullable TTarget pKay) {
        this.dispatcher.onFinished(pKay);
    }
}
