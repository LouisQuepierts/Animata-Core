package net.quepierts.animata.core.service;

import net.quepierts.animata.core.service.loader.ServiceEnvironment;
import net.quepierts.animata.core.service.loader.Singleton;

@Singleton
public interface IAnimataTimeProvider {
    float getCountedTime();

    boolean isPaused();

    @ServiceEnvironment(ServiceEnvironment.Environment.CLIENT)
    interface Client extends IAnimataTimeProvider {

    }
}
