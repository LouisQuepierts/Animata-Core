package net.quepierts.animata.core.animation.animator.control;

public interface AnimationHandle<K, T> {
    float getProgress();

    void setProgress(float progress);

    boolean isRunning();

    default boolean isFinished() {
        return !this.isRunning();
    }
}
