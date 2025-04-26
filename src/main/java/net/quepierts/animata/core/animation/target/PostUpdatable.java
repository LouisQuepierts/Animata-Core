package net.quepierts.animata.core.animation.target;

@FunctionalInterface
public interface PostUpdatable {
    void onPostUpdate(float pTime, boolean pUpdated);
}
