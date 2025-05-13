package net.quepierts.animata.core.animation.animator.control;

public interface AnimationHandle {
    float getProgress();

    void setProgress(float progress);

    boolean isRunning();

    default boolean isFinished() {
        return !this.isRunning();
    }
}
