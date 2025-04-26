package net.quepierts.animata.core.util;

public class Generic {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(int size) {
        return (T[]) new Object[size];
    }
}
