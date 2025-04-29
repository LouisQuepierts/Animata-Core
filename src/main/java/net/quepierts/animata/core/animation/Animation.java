package net.quepierts.animata.core.animation;

import net.quepierts.animata.core.animation.binding.Source;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Animation {
    default boolean isFinished(float pTime) {
        return pTime >= this.getLength().getTick();
    }

    Duration getLength();

    void getSources(List<Source> pOut);

    @Nullable Source getSource(String name);
}
