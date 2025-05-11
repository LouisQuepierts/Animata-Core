package net.quepierts.animata.core.animation.animator.extension;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.quepierts.animata.core.animation.animator.base.ExtensibleAnimator;
import net.quepierts.animata.core.animation.cache.AnimationCacheRegistrar;
import net.quepierts.animata.core.util.Generic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AnimationExtensionDispatcher<TTarget, TAnimation> {
    private static final int INITIAL_CAPACITY = 16;

    private final ExtensibleAnimator<TTarget, TAnimation> animator;

    private final List<LifecycleHook<ExtensibleAnimator<TTarget, TAnimation>, TTarget, TAnimation>> lifecycleHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<UpdateHook<ExtensibleAnimator<TTarget, TAnimation>>> updateHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<ProcessHook<ExtensibleAnimator<TTarget, TAnimation>>> processHooks = new ArrayList<>(INITIAL_CAPACITY);
    private final List<ApplyHook<ExtensibleAnimator<TTarget, TAnimation>>> applyHooks = new ArrayList<>(INITIAL_CAPACITY);

    public void register(
            @NotNull AnimatorExtension<TTarget, TAnimation> pExtension,
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
            @Nullable TTarget pKey,
            @NotNull TAnimation pAnimation
    ) {
        for (val hook : this.lifecycleHooks) {
            hook.onPlay(this.animator, pKey, pAnimation);
        }
    }

    public void onStop(@Nullable TTarget pKey) {
        for (val hook : this.lifecycleHooks) {
            hook.onStop(this.animator, pKey);
        }
    }

    public void onFinished(@Nullable TTarget pKey) {
        for (val hook : this.lifecycleHooks) {
            hook.onFinished(this.animator, pKey);
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
