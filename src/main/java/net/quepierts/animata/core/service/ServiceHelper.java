package net.quepierts.animata.core.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
public final class ServiceHelper {
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        log.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
