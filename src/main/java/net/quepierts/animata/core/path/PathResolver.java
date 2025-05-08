package net.quepierts.animata.core.path;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

public class PathResolver {
    public static @NotNull Result resolve(
            @NotNull String pPath,
            @NotNull PathResolvable pRoot
    ) {
        if (pPath.isBlank()) return Result.EMPTY;

        String[] names = pPath.split("\\.");
        PathResolvable target = pRoot;
        int i = 0;

        while (i < names.length) {
            target = target.getChild(names[i]);
            if (target == null) return Result.fail(names, i);
            i++;
        }

        return Result.success(names, target);
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Result {
        public static final Result EMPTY = new Result(new String[0], null, -1);

        private static Result success(String[] names, PathResolvable target) {
            return new Result(names, target, names.length - 1);
        }

        private static Result fail(String[] names, int index) {
            return new Result(names, null, index);
        }

        private final String[] names;
        private final PathResolvable target;
        private final int index;

        public boolean isSuccess() {
            return target != null;
        }

        public int breakPoint() {
            return index;
        }
    }
}
