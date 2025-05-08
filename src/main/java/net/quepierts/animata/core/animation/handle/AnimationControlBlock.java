package net.quepierts.animata.core.animation.handle;

public interface AnimationControlBlock<TKey, TAnimation>
        extends AnimationHandle<TKey, TAnimation> {
    void reset();

    void release();

    void update(float pDeltaTime);

    interface Processable<TKey, TAnimation> extends AnimationControlBlock<TKey, TAnimation> {
        void process();
    }

    interface Applicable<TKey, TAnimation> extends AnimationControlBlock<TKey, TAnimation> {
        void apply();
    }
}
