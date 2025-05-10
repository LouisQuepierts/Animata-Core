package net.quepierts.animata.core.animation.animator.extension;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.quepierts.animata.core.animation.animator.Animator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;
import net.quepierts.animata.core.util.Generic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AnimationExtensionDispatcher<TAnimator extends Animator<TKey, TAnimation>, TKey, TAnimation> {
    private static final int INITIAL_CAPACITY = 16;

    private final TAnimator animator;

    private final List<LifecycleHook<TAnimator, TKey, TAnimation>> lifecycleHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<UpdateHook<TAnimator>> updateHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<ProcessHook<TAnimator>> processHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<ApplyHook<TAnimator>> applyHooks = new ArrayList<>(INITIAL_CAPACITY);

    public void register(
            @NotNull AnimatorExtension<TAnimator, TKey, TAnimation> pExtension,
            @NotNull AnimationCacheRegistrar pCacheRegistrar
    ) {
        pExtension.onRegister(this.animator, pCacheRegistrar);

        if (pExtension instanceof LifecycleHook) {
            this.lifecycleHooks.add(Generic.cast(pExtension));
            this.lifecycleHooks.sort(Extension::compareTo);
        }

        if (pExtension instanceof UpdateHook) {
            this.updateHooks.add(Generic.cast(pExtension));
            this.updateHooks.sort(Extension::compareTo);
        }

        if (pExtension instanceof ProcessHook) {
            this.processHooks.add(Generic.cast(pExtension));
            this.processHooks.sort(Extension::compareTo);
        }

        if (pExtension instanceof ApplyHook) {
            this.applyHooks.add(Generic.cast(pExtension));
            this.applyHooks.sort(Extension::compareTo);
        }
    }

    public void onPlay(
            @Nullable TKey pKey,
            @NotNull TAnimation pAnimation,
            float pGlobalTime
    ) {
        for (val hook : this.lifecycleHooks) {
            hook.onPlay(this.animator, pKey, pAnimation);
        }
    }

    public void onStop(
            @Nullable TKey pKey,
            float pGlobalTime
    ) {
        for (val hook : this.lifecycleHooks) {
            hook.onStop(this.animator, pKey);
        }
    }

    public void onPreUpdate(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.updateHooks) {
            hook.onPreUpdate(this.animator, pGlobalTime, pDeltaTime);
        }
    }

    public void onPostUpdate(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.updateHooks) {
            hook.onPostUpdate(this.animator, pGlobalTime, pDeltaTime);
        }
    }

    public void onPreProcess(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.processHooks) {
            hook.onPreProcess(this.animator, pGlobalTime, pDeltaTime);
        }
    }

    public void onPostProcess(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.processHooks) {
            hook.onPostProcess(this.animator, pGlobalTime, pDeltaTime);
        }
    }

    public void onPreApply(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.applyHooks) {
            hook.onPreApply(this.animator, pGlobalTime, pDeltaTime);
        }
    }

    public void onPostApply(float pGlobalTime, float pDeltaTime) {
        for (val hook : this.applyHooks) {
            hook.onPostApply(this.animator, pGlobalTime, pDeltaTime);
        }
    }
}
