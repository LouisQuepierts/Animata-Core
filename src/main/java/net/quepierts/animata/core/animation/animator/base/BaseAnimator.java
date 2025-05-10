package net.quepierts.animata.core.animation.animator.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class BaseAnimator<TKey, TAnimation> implements Animator<TKey, TAnimation> {

    protected final @NotNull IAnimataTimeProvider timeProvider;

    @Getter(AccessLevel.PROTECTED) private float lastUpdatedTime;
    @Getter(AccessLevel.PROTECTED) private float deltaTime;

    protected abstract void onUpdate();

    @Override
    public final void update() {
        float ticks = this.timeProvider.getCountedTime();
        this.deltaTime = ticks - this.lastUpdatedTime;

        this.onUpdate();

        this.lastUpdatedTime = ticks;
    }
}
