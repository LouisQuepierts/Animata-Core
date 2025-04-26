package net.quepierts.animata.core.registry;

import java.util.function.Supplier;

public interface Registrar<T> {
    Holder<T> register(String name, Supplier<T> supplier);

    void register();
}
