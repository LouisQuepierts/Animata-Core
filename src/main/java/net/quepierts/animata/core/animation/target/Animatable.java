package net.quepierts.animata.core.animation.target;

import net.quepierts.animata.core.path.PathResolvable;

import java.util.List;

public interface Animatable extends PathResolvable, BindableTarget {

    void getChildren(List<Animatable> pOut);

}
