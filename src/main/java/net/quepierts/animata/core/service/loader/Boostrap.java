package net.quepierts.animata.core.service.loader;

import java.util.ServiceLoader;

public class Boostrap {
    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
