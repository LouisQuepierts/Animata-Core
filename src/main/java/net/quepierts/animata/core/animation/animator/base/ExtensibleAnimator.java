package net.quepierts.animata.core.animation.animator.base;

import net.quepierts.animata.core.animation.animator.extension.AnimatorExtension;

/**
 * animator that can add extension
 * @param <TTarget> the identity key type used to distinguish animation control blocks
 * @param <TAnimation> the animation type (e.g., AnimationClip or AnimationSequence)
 */
@SuppressWarnings("unused")
public interface ExtensibleAnimator<TTarget, TAnimation> {
    void addExtension(AnimatorExtension<TTarget, TAnimation> pExtension);
}
