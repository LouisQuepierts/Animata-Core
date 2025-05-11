package net.quepierts.animata.core.animation.animator.control;

public interface AnimationControlBlock<TTarget, TAnimation>
        extends AnimationHandle<TTarget, TAnimation> {
    void reset();

    void release();

    void update(float pDeltaTime);

    default void process() {}

    default void apply() {}
}
