package net.quepierts.animata.core.animation.timeline;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.quepierts.animata.core.animation.core.IAnimation;
import net.quepierts.animata.core.animation.binding.ISource;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableTimelineAnimation implements IAnimation {
    private final Object2IntMap<String> keys;
    private final ObjectList<ITrack> tracks;

    @Getter
    private final Duration length;

    public static ImmutableTimelineAnimation of(ITrack... tracks) {
        Object2IntMap<String> keys = new Object2IntOpenHashMap<>(tracks.length);
        Duration length = Duration.ZERO;
        for (ITrack track : tracks) {
            String name = track.getName();

            if (keys.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate track name: " + name);
            }

            keys.put(name, keys.size());
            length = Duration.max(length, track.getLength());
        }
        return new ImmutableTimelineAnimation(keys, ObjectList.of(tracks), length);
    }

    @Override
    public void getSources(List<ISource> pOut) {
        pOut.addAll(this.tracks);
    }

    @Override
    public @Nullable ISource getSource(String name) {
        int i = this.keys.getOrDefault(name, -1);
        return i != -1 ? this.tracks.get(i) : null;
    }
}
