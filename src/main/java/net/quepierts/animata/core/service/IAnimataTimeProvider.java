package net.quepierts.animata.core.service;

import net.quepierts.animata.core.service.loader.RuntimeEnvironment;
import net.quepierts.animata.core.service.loader.ServiceEnvironment;
import net.quepierts.animata.core.service.loader.Singleton;

@Singleton
public interface IAnimataTimeProvider {
    float getCountedTime();

    @ServiceEnvironment(RuntimeEnvironment.CLIENT)
    interface Client extends IAnimataTimeProvider {

    }
}
