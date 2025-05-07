package net.quepierts.animata.core.animation.blend;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import net.quepierts.animata.core.animation.Animation;
import net.quepierts.animata.core.animation.binding.Source;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class MultiAnimation implements Animation {
    private final Map<String, Animation> string2animation;
    private final Object2IntMap<String> string2int;
    private final Animation[] int2animation;

    @Getter private final Duration length;

    public MultiAnimation(Map<String, Animation> string2animation) {
        this.string2animation = string2animation;
        this.string2int = new Object2IntOpenHashMap<>();
        this.int2animation = new Animation[string2animation.size()];

        int i = 0;
        Duration length = Duration.ZERO;
        for (Map.Entry<String, Animation> entry : string2animation.entrySet()) {
            this.string2int.put(entry.getKey(), i);
            this.int2animation[i] = entry.getValue();
            i++;

            length = Duration.max(length, entry.getValue().getLength());
        }

        this.length = length;
    }

    @Override
    public void getSources(Collection<Source> pOut) {

    }

    @Override
    public @Nullable Source getSource(String name) {
        return null;
    }
}
