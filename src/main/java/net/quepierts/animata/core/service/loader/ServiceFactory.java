package net.quepierts.animata.core.service.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.service.IRuntimeEnvironmentDetector;
import net.quepierts.animata.core.util.Generic;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public final class ServiceFactory {
    private static final Map<Class<?>, Delegate> suppliers = new IdentityHashMap<>();
    private static final Map<Class<?>, Object> singletons = new IdentityHashMap<>();
    private static final Map<Class<?>, Class<?>> delegate = new IdentityHashMap<>();

    public static <T> void register(Class<T> clazz, Supplier<Supplier<T>> supplier) {
        ServiceFactory.check(clazz);

        Delegate delegate = new Delegate(Generic.cast(supplier));
        suppliers.put(clazz, delegate);

        if (clazz.isAnnotationPresent(Singleton.class)) {
            delegate.serviceType = ServiceType.SINGLETON;
        }
    }

    public static <T> void register(Class<T> clazz, T instance) {
        ServiceFactory.check(clazz);
        singletons.put(clazz, instance);
    }

    public static <T> T load(Class<T> clazz) {
        Object singleton = singletons.get(clazz);
        if (singleton != null) {
            return Generic.cast(singleton);
        }

        Class<? extends T> delegateClass = getDelegateClass(clazz);
        Delegate delegate = suppliers.get(delegateClass);

        if (delegate != null) {
            Object service = delegate.supplier.get().get();

            if (service == null) {
                throw new RuntimeException("Failed to load service for " + clazz.getName());
            }

            if (delegate.serviceType == ServiceType.UNKNOWN) {
                boolean isSingleton = service.getClass().isAnnotationPresent(Singleton.class);
                delegate.serviceType = isSingleton ? ServiceType.SINGLETON : ServiceType.INSTANCE;

                if (isSingleton) {
                    singletons.put(clazz, service);
                    singletons.put(delegateClass, service);
                }
            } else if (delegate.serviceType == ServiceType.SINGLETON) {
                singletons.put(clazz, service);
                singletons.put(delegateClass, service);
            }

            return Generic.cast(service);
        }

        final T loadedService = Boostrap.load(delegateClass);
        if (loadedService.getClass().isAnnotationPresent(Singleton.class)) {
            singletons.put(clazz, loadedService);
            singletons.put(delegateClass, loadedService);
        }

        return loadedService;
    }

    private static void check(Class<?> clazz) {
        if (clazz.isAnnotationPresent(FrameworkRequired.class)) {
            throw new RuntimeException(clazz.getName() + "Is a framework required service. Could not register by ServiceFactory.register method!");
        }
    }

    private static <T> Class<? extends T> getDelegateClass(Class<T> base) {
        Class<?> fallback = ServiceFactory.delegate.get(base);
        if (fallback != null) {
            return Generic.cast(fallback);
        }

        boolean client = IRuntimeEnvironmentDetector.INSTANCE.isClient();
        ServiceEnvironment.Environment env = client ? ServiceEnvironment.Environment.CLIENT : ServiceEnvironment.Environment.SERVER;

        fallback = base;
        for (Class<?> sub : base.getDeclaredClasses()) {
            if (!base.isAssignableFrom(sub)) {
                continue;
            }

            ServiceEnvironment environment = sub.getAnnotation(ServiceEnvironment.class);
            if (environment == null || environment.value() == env) {
                fallback = sub;
            }
        }
        ServiceFactory.delegate.put(base, fallback);
        return Generic.cast(fallback);
    }

    @RequiredArgsConstructor
    private static final class Delegate {
        private final Supplier<Supplier<?>> supplier;
        private ServiceType serviceType = ServiceType.UNKNOWN;
    }

    private enum ServiceType {
        UNKNOWN,
        INSTANCE,
        SINGLETON
    }
}
