package net.quepierts.animata.core.animation.skeleton;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public sealed class SkeletonMask {
    private final Set<String> bones;
    private final boolean inverted;

    public static SkeletonMask getDefault() {
        return Default.INSTANCE;
    }

    public boolean isActive(String key) {
        return this.bones.contains(key) == this.inverted;
    }

    public boolean isActive(@NotNull AnimataBone bone) {
        return this.bones.contains(bone.getName()) == this.inverted;
    }

    private static final class Default extends SkeletonMask {
        private static final Default INSTANCE = new Default();

        private Default() {
            super(Collections.emptySet(), true);
        }

        @Override
        public boolean isActive(String key) {
            return true;
        }

        @Override
        public boolean isActive(@NotNull AnimataBone bone) {
            return true;
        }
    }
}
