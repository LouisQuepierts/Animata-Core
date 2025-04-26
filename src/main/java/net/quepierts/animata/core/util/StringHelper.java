package net.quepierts.animata.core.util;

public class StringHelper {
    public static String getSneakCaseName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return getSneakCaseName(object.getClass());
        }
    }

    public static String getSneakCaseName(Class<?> type) {
        String simpleName = type.getSimpleName();
        return simpleName
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    public static String toSneakCase(String string) {
        return string
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }
}
