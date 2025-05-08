package net.quepierts.animata.core.animation.animator;

/**
 * Represents a generic animation controller capable of managing and playing animations of type {@code T}
 * associated with identity keys of type {@code K}.
 * <p>
 * This interface defines a three-phase animation evaluation model:
 * {@link #update()}, {@link #process()}, and {@link #apply()}, which are intended to be called in a specific order
 * per animation tick/frame to separate evaluation, internal computation, and external application of animation data.
 * </p>
 *
 * <p><strong>Three-phase evaluation:</strong></p>
 * <ul>
 *     <li><b>{@link #update()}:</b> Evaluates the animation timeline based on elapsed time (delta).
 *         This step is responsible for calculating raw animation values per clip or sequence.
 *         Skipped if the system is paused or delta time is zero.</li>
 *     <li><b>{@link #process()}:</b> Performs any intermediate post-processing, such as blending, IK solving,
 *         or redirection logic within the animation system, especially in external caches. This stage is optional
 *         in simpler systems but required for complex setups.</li>
 *     <li><b>{@link #apply()}:</b> Transfers the final computed values from the animation system to the actual
 *         target objects (e.g., updating skeleton transforms, UI element properties, etc.). This is always executed
 *         regardless of system pause state if visual updates are needed.</li>
 * </ul>
 *
 * <p>
 * Implementations may define how animations are played and paused through the {@link PlayControl} and {@link PauseControl}
 * sub-interfaces. Consumers of this interface should invoke the methods in order: {@code update()}, then {@code process()},
 * then {@code apply()} per frame or tick.
 * </p>
 *
 * @param <TKey> the identity key type used to distinguish animation control blocks
 * @param <TAnimation> the animation type (e.g., AnimationClip or AnimationSequence)
 */
@SuppressWarnings("unused")
public interface Animator<TKey, TAnimation>
        extends PlayControl<TKey, TAnimation>, PauseControl<TKey>, ExtensibleAnimator<TKey, TAnimation> {

    /**
     * Evaluates the internal state of all active animations based on the current time delta.
     * This method should be called once per frame/tick before {@link #process()} or {@link #apply()}.
     * Skips evaluation if no time has passed or the system is paused.
     */
    void update();

    /**
     * Performs any post-processing steps such as blending, constraint solving, or redirection logic.
     * Typically used in more complex animation systems. Called after {@link #update()} and before {@link #apply()}.
     */
    void process();

    /**
     * Applies the final computed animation values to the actual target objects or properties.
     * This method should always be called last in the animation frame cycle.
     */
    void apply();

    /**
     * Returns {@code true} if any animations are currently active and updating.
     *
     * @return whether the animator is currently running.
     */
    boolean isRunning();
}
