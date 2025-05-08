package net.quepierts.animata.core.animation;

public interface AnimationHandle<K, T> {
    float getProgress();

    void setProgress(float progress);

    boolean isRunning();

    default boolean isFinished() {
        return !this.isRunning();
    }
}
