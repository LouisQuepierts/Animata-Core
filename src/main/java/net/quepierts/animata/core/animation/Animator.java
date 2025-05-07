package net.quepierts.animata.core.animation;

public interface Animator {
    void play(AnimationSequence pAnimationSequence);

    void stop();

    void update();

    void apply();

    boolean isRunning();
}
