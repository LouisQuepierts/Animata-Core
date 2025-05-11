package net.quepierts.animata.core.animation.animator.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.service.IAnimataTimeProvider;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractAnimator<TKey, TAnimation> implements Animator<TKey, TAnimation> {

    protected final @NotNull IAnimataTimeProvider timeProvider;

    @Getter(AccessLevel.PROTECTED) float lastUpdatedTime;
    @Getter(AccessLevel.PROTECTED) float deltaTime;

    protected abstract boolean onUpdate();

    @Override
    public void update() {
        float ticks = this.timeProvider.getCountedTime();
        this.deltaTime = ticks - this.lastUpdatedTime;

        this.onUpdate();

        this.lastUpdatedTime = ticks;
    }
}
