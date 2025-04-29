package net.quepierts.animata.core.animation.extension;

import net.quepierts.animata.core.animation.Animation;
import net.quepierts.animata.core.animation.Animator;

public interface AnimatorExtension {
    void onPlay(Animator pAnimator, Animation pAnimation);

    void onUpdate(Animator pAnimator, float pDeltaTime);

    void onApply(Animator pAnimator);

    void onStop(Animator pAnimator);
}
