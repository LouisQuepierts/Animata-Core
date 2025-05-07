package net.quepierts.animata.core.animation.blend;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.binding.AnimationClip;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class MultiAnimationSequence implements AnimationSequence {
    private final Map<String, AnimationSequence> string2animation;
    private final Object2IntMap<String> string2int;
    private final AnimationSequence[] int2AnimationSequence;

    @Getter private final Duration length;

    public MultiAnimationSequence(Map<String, AnimationSequence> string2animation) {
        this.string2animation = string2animation;
        this.string2int = new Object2IntOpenHashMap<>();
        this.int2AnimationSequence = new AnimationSequence[string2animation.size()];

        int i = 0;
        Duration length = Duration.ZERO;
        for (Map.Entry<String, AnimationSequence> entry : string2animation.entrySet()) {
            this.string2int.put(entry.getKey(), i);
            this.int2AnimationSequence[i] = entry.getValue();
            i++;

            length = Duration.max(length, entry.getValue().getLength());
        }

        this.length = length;
    }

    @Override
    public void getAnimationClips(Collection<AnimationClip> pOut) {

    }

    @Override
    public @Nullable AnimationClip getAnimationClip(String name) {
        return null;
    }
}
