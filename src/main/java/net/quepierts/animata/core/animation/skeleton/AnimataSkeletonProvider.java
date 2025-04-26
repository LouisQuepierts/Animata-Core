package net.quepierts.animata.core.animation.skeleton;

public interface AnimataSkeletonProvider {
    default AnimataSkeleton getAnimataSkeleton() {
        return AnimataSkeleton.DEFAULT;
    }
}
