package net.quepierts.animata.core.animation.target;

@FunctionalInterface
public interface PreUpdatable {
    void onPreUpdate(float pTime, boolean pUpdated);
}
