package net.quepierts.animata.core.animation.core;

public interface IAnimator {
    void play(IAnimation pAnimation);

    void stop();

    void terminate();

    void update();

    void apply();

    boolean isRunning();
}
