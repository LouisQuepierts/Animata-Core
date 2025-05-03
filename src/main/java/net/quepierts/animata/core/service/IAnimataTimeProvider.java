package net.quepierts.animata.core.service;

import net.quepierts.animata.core.service.loader.Singleton;

@Singleton
public interface IAnimataTimeProvider {
    float getCountedTime();
}
