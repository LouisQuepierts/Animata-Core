package net.quepierts.animata.core.client;

import net.quepierts.animata.core.service.AnimataTimeProvider;
import net.quepierts.animata.core.service.ServiceHelper;

public class ClientTickHandler {
    public static final AnimataTimeProvider IMPLEMENT = ServiceHelper.load(AnimataTimeProvider.class);
}
