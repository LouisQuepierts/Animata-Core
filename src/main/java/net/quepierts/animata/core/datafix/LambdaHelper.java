package net.quepierts.animata.core.datafix;

import com.mojang.datafixers.util.*;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LambdaHelper {

    private static final Class<?>[] FUNCTIONS = {
            Function.class,
            BiFunction.class,
            Function3.class,
            Function4.class,
            Function5.class,
            Function6.class,
            Function7.class,
            Function8.class,
            Function9.class,
            Function10.class,
            Function11.class,
            Function12.class,
            Function13.class,
            Function14.class,
            Function15.class,
            Function16.class,
    };

    public static Object lambda(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Method pMethod,
            @NotNull Class<?> pTarget
    ) {
        try {
            Class<?>[] parameters = new Class[pMethod.getParameterCount() + 1];
            int count = pMethod.getParameterCount();
            parameters[0] = pTarget;
            Class<?>[] types = pMethod.getParameterTypes();

            if (count >= 0) {
                System.arraycopy(types, 0, parameters, 1, count);
            }

            MethodHandle handle = pLookup.unreflect(pMethod);

            MethodType type = MethodType.methodType(pMethod.getReturnType(), parameters);
            Class<?> functionType = FUNCTIONS[count];
            CallSite site = LambdaMetafactory.metafactory(
                    pLookup,
                    "apply",
                    MethodType.methodType(functionType),
                    type,
                    handle,
                    handle.type()
            );

            return functionType.cast(site.getTarget().invoke());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object lambda(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Constructor<?> pConstructor
    ) {
        try {
            MethodType type = MethodType.methodType(pConstructor.getDeclaringClass(), pConstructor.getParameterTypes());
            int count = pConstructor.getParameterCount();

            MethodHandle handle = pLookup.unreflectConstructor(pConstructor);

            Class<?> functionType = FUNCTIONS[count - 1];
            CallSite site = LambdaMetafactory.metafactory(
                    pLookup,
                    "apply",
                    MethodType.methodType(functionType),
                    type,
                    handle,
                    handle.type()
            );

            return functionType.cast(site.getTarget().invoke());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object lambda(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Field pField,
            @NotNull Class<?> pTarget
    ) {
        try {
            MethodType type = MethodType.methodType(pField.getType(), pTarget);
            MethodHandle handle = pLookup.unreflectGetter(pField);

            CallSite site = LambdaMetafactory.metafactory(
                    pLookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    type.erase(),
                    handle,
                    handle.type()
            );

            return (Function) site.getTarget().invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object lambda(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Method pMethod
    ) {
        try {
            MethodType type = MethodType.methodType(pMethod.getReturnType(), pMethod.getParameterTypes());
            int count = pMethod.getParameterCount();

            MethodHandle handle = pLookup.unreflect(pMethod);

            Class<?> functionType = FUNCTIONS[count];
            CallSite site = LambdaMetafactory.metafactory(
                    pLookup,
                    "apply",
                    MethodType.methodType(functionType),
                    type,
                    handle,
                    handle.type()
            );

            return functionType.cast(site.getTarget().invoke());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
