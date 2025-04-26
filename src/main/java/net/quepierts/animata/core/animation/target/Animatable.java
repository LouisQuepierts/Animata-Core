package net.quepierts.animata.core.animation.target;

import net.quepierts.animata.core.animation.path.PathResolvable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Animatable extends PathResolvable {

    void getChildren(List<Animatable> pOut);

    @Nullable Animatable getParent();
}
