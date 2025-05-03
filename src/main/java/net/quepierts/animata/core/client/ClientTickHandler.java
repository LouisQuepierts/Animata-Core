package net.quepierts.animata.core.client;

import net.quepierts.animata.core.service.IAnimataTimeProvider;
import net.quepierts.animata.core.service.loader.ServiceFactory;

public class ClientTickHandler {
    public static final IAnimataTimeProvider IMPLEMENT = ServiceFactory.load(IAnimataTimeProvider.class);
}
