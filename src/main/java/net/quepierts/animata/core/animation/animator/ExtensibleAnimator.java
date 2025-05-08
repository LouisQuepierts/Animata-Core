package net.quepierts.animata.core.animation.animator;

import net.quepierts.animata.core.animation.extension.AnimatorExtension;

public interface ExtensibleAnimator<TKey, TAnimation> {
    default void register(AnimatorExtension<? extends Animator<TKey, TAnimation>, TKey, TAnimation> pExtension) {

    }
}
