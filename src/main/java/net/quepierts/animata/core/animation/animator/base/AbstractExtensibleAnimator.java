package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.animator.control.AnimationControlBlock;
import net.quepierts.animata.core.animation.animator.control.AnimationHandle;
import net.quepierts.animata.core.animation.animator.extension.AnimationExtensionDispatcher;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExtensibleAnimator<TKey, TAnimation>
        extends AbstractAnimator<TKey, TAnimation>
        implements ExtensibleAnimator<TKey, TAnimation> {

    private final AnimationExtensionDispatcher<AbstractExtensibleAnimator<TKey, TAnimation>, TKey, TAnimation> dispatcher;

    public AbstractExtensibleAnimator(@NotNull IAnimataTimeProvider pTimeProvider) {
        super(pTimeProvider);
        this.dispatcher = new AnimationExtensionDispatcher<>(this);
    }

    protected abstract boolean onApply();

    protected abstract boolean onProcess();

    protected abstract AnimationControlBlock<TKey, TAnimation> onPlay(
            @Nullable TKey pKey,
            @NotNull TAnimation pAnimation
    );

    protected abstract void onStop(@Nullable TKey pKey);

    @Override
    public void update() {
        float time = this.timeProvider.getCountedTime();
        this.deltaTime = this.lastUpdatedTime - time;

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
    public AnimationHandle<TKey, TAnimation> play(@Nullable TKey pKey, @NotNull TAnimation pAnimation) {
        this.dispatcher.onPlay(pKey, pAnimation);
        return this.onPlay(pKey, pAnimation);
    }

    @Override
    public void stop(@Nullable TKey pKey) {
        this.dispatcher.onStop(pKey);
        this.onStop(pKey);
    }

    protected final void onFinished(@Nullable TKey pKay) {
        this.dispatcher.onFinished(pKay);
    }
}
