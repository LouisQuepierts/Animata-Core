package net.quepierts.animata.core.datafix.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface XMap {
    Class<?> value() default Void.class;
}
