package net.quepierts.animata.core.animation.core;

import net.quepierts.animata.core.animation.binding.ISource;
import net.quepierts.animata.core.data.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IAnimation {
    default boolean isFinished(float pTime) {
        return pTime >= this.getLength().getTick();
    }

    Duration getLength();

    void getSources(List<ISource> pOut);

    @Nullable ISource getSource(String name);
}
