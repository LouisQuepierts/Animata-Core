package net.quepierts.animata.core.animation.path;

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

        int colon = pPath.indexOf(':');

        PathResolvable target = pRoot;
        String path = pPath;
        if (colon != -1) {
            String name = pPath.substring(0, colon);
            if (!name.equals(target.getName())) {
                return Result.EMPTY;
            }

            path = pPath.substring(colon + 1);
        }

        String[] names = path.split("\\.");
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
