package net.quepierts.animata.core.service;

import net.quepierts.animata.core.service.loader.Boostrap;
import net.quepierts.animata.core.service.loader.FrameworkRequired;
import net.quepierts.animata.core.service.loader.Singleton;

@Singleton
@FrameworkRequired
public interface IRuntimeEnvironmentDetector {
    IRuntimeEnvironmentDetector INSTANCE = Boostrap.load(IRuntimeEnvironmentDetector.class);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Check if the game is currently in a client environment.
     *
     * @return True if in a client environment, false otherwise.
    */
    boolean isClient();
}
