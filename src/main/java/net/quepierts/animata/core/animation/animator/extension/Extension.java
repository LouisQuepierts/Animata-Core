package net.quepierts.animata.core.animation.animator.extension;

public interface Extension extends Comparable<Extension> {
    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(Extension o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
