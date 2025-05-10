package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.animator.extension.AnimatorExtension;

public interface ExtensibleAnimator<TKey, TAnimation> {
    default void addExtension(AnimatorExtension<? extends ExtensibleAnimator<TKey, TAnimation>, TKey, TAnimation> pExtension) {

    }
}
