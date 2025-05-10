package net.quepierts.animata.core.animation.timeline;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.AnimationClip;
import net.quepierts.animata.core.animation.runtime.RuntimeContext;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableTimelineAnimationSequence implements AnimationSequence {
    private final Object2IntMap<String> keys;
    private final ObjectList<Track> tracks;

    @Getter
    private final Duration length;

    public static ImmutableTimelineAnimationSequence of(Track... tracks) {
        Object2IntMap<String> keys = new Object2IntOpenHashMap<>(tracks.length);
        Duration length = Duration.ZERO;
        for (Track track : tracks) {
            String name = track.getName();

            if (keys.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate track name: " + name);
            }

            keys.put(name, keys.size());
            length = Duration.max(length, track.getLength());
        }
        return new ImmutableTimelineAnimationSequence(keys, ObjectList.of(tracks), length);
    }

    @Override
    public boolean isFinished(RuntimeContext pContext) {
        return pContext.getProgress() >= this.length.getTick();
    }

    @Override
    public void getAnimationClips(@NotNull Collection<AnimationClip> pOut) {
        pOut.addAll(this.tracks);
    }

    @Override
    public @Nullable AnimationClip getAnimationClip(@NotNull String name) {
        int i = this.keys.getOrDefault(name, -1);
        return i != -1 ? this.tracks.get(i) : null;
    }
}
