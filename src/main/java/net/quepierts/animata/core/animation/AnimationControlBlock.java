package net.quepierts.animata.core.animation;

public interface AnimationControlBlock<TKey, TAnimation>
        extends AnimationHandle<TKey, TAnimation> {
    void reset();

    void release();

    void update(float pCurrentTime);

    interface Processable<TKey, TAnimation> extends AnimationControlBlock<TKey, TAnimation> {
        void process();
    }

    interface Applicable<TKey, TAnimation> extends AnimationControlBlock<TKey, TAnimation> {
        void apply();
    }
}
