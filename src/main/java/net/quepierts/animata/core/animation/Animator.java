package net.quepierts.animata.core.animation;

public interface Animator {
    void play(Animation pAnimation);

    void stop();

    void terminate();

    void update();

    void apply();

    boolean isRunning();
}
