package net.quepierts.animata.core.animation.animator.control;

public interface AnimationControlBlock<TKey, TAnimation>
        extends AnimationHandle<TKey, TAnimation> {
    void reset();

    void release();

    void update(float pDeltaTime);

    default void process() {}

    default void apply() {}
}
