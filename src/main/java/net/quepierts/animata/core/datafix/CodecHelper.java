package net.quepierts.animata.core.datafix;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.Kind1;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.SneakyThrows;
import net.quepierts.animata.core.datafix.annotations.CodecConstructor;
import net.quepierts.animata.core.datafix.annotations.SerializeField;
import net.quepierts.animata.core.datafix.annotations.CodecSerializable;
import net.quepierts.animata.core.util.Generic;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

public class CodecHelper {
    private static final Map<Class<?>, Codec<?>> CODECS;

    private static final MethodHandle[] METHOD_GROUP;
    private static final MethodHandle[] METHOD_APPLY;

    public static void scan(Class<?> pClass) {
        for (Field field : pClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!Codec.class.isAssignableFrom(field.getType())) {
                continue;
            }

            try {
                ParameterizedType type = ((ParameterizedType) field.getGenericType());
                CODECS.putIfAbsent((Class<?>) type.getActualTypeArguments()[0], (Codec<?>) field.get(null));
            } catch (Exception ignored) {}
        }
    }

    public static <T> @NotNull Codec<T> getCodec(Class<T> pClass) {
        if (pClass.isArray()) {
            Class<?> componentType = pClass.componentType();
            Codec<?> codec = getRawCodec(componentType);
            return Generic.cast(codec.listOf());
        }

        return getRawCodec(pClass);
    }

    public static <T> Codec<T> register(Class<T> pClass, Codec<T> pCodec) {
        return Generic.cast(CODECS.putIfAbsent(pClass, pCodec));
    }

    public static <T> Codec<T> codec(Class<T> pClass) {
        if (pClass.isRecord()) {
            Codec<T> codec = record(pClass);
            CODECS.put(pClass, codec);
            return codec;
        }

        return ordinary(pClass);
    }

    public static <T> Codec<T> ordinary(Class<T> pClass) {
        if (pClass.isInterface() || Modifier.isAbstract(pClass.getModifiers())) {
            throw new UnsupportedOperationException("Unsupported codec type " + pClass);
        }

        List<Field> serializedFields = new ArrayList<>();
        for (Field field : pClass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.isAnnotationPresent(SerializeField.class)) {
                serializedFields.add(field);
            }
        }

        Map<String, Method> methods = new HashMap<>();
        for (Method method : pClass.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            methods.put(method.getName(), method);
        }

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        List<Object> parameters = new ArrayList<>(serializedFields.size() + 1);

        for (Field field : serializedFields) {
            Class<?> type = field.getType();
            Codec<?> codec = getCodec(type);

            String getterName = field.getAnnotation(SerializeField.class).getter();
            Object lambda;
            if (!getterName.isBlank()) {
                Method method = methods.get(getterName);

                if (method == null) {
                    throw new RuntimeException("Missing getter " + getterName);
                }

                lambda = LambdaHelper.lambda(lookup, method, pClass);
            } else {
                // try find getters
                Method method = methods.get(field.getName());

                if (method == null) {
                    String upperName = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                    method = methods.get("get" + upperName);
                }

                if (method == null) {
                    lambda = LambdaHelper.lambda(lookup, field, pClass);
                } else {
                    lambda = LambdaHelper.lambda(lookup, method, pClass);
                }
            }

            parameters.add(codec.fieldOf(field.getName()).forGetter(Generic.cast(lambda)));
        }

        Object constructor = findConstructor(lookup, pClass, serializedFields);
        return collect(lookup, parameters, constructor);
    }

    public static <T> Codec<T> record(Class<T> pClass) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        
        RecordComponent[] components = pClass.getRecordComponents();
        List<Object> parameters = new ArrayList<>(components.length + 1);


        Object constructor = LambdaHelper.lambda(lookup, pClass.getConstructors()[0]);

        for (RecordComponent component : components) {
            Class<?> type = component.getType();
            Codec<?> codec = getCodec(type);

            String name = getFieldName(component);
            Object lambda = LambdaHelper.lambda(lookup, component.getAccessor(), pClass);
            parameters.add(codec.fieldOf(name).forGetter(Generic.cast(lambda)));
        }

        return collect(lookup, parameters, constructor);
    }

    private static <T> Object findConstructor(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Class<T> pClass,
            @NotNull List<Field> pFields
    ) {
        Method[] methods = pClass.getDeclaredMethods();
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (!method.isAnnotationPresent(CodecConstructor.class)) {
                continue;
            }

            if (method.getParameterCount() != pFields.size()) {
                throw new RuntimeException("Invalid constructor " + method + ". Expected parameters " + pFields.size());
            }

            method.trySetAccessible();
            return LambdaHelper.lambda(pLookup, method);
        }

        Constructor<?>[] constructors = pClass.getDeclaredConstructors();
        Constructor<?> fallback = constructors[0];

        Constructor<?> selected = null;
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(CodecConstructor.class)) {
                selected = constructor;
                break;
            }
        }

        if (selected == null) {
            selected = fallback;
        }

        if (selected.getParameterCount() != pFields.size()) {
            throw new RuntimeException("Invalid constructor " + selected + ". Expected parameters " + pFields.size());
        }

        selected.trySetAccessible();

        return LambdaHelper.lambda(pLookup, selected);
    }

    @SneakyThrows
    private static <T> Codec<T> collect(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull List<Object> pParameters,
            @NotNull Object pConstructor
    ) {
        int count = pParameters.size() - 1;

        MethodHandle groupCall = METHOD_GROUP[count];
        RecordCodecBuilder.Instance<T> instance = RecordCodecBuilder.instance();

        pParameters.addFirst(instance);
        Object grouped = groupCall.invokeWithArguments(pParameters);

        MethodHandle applyCall = METHOD_APPLY[count];
        Object applied = applyCall.invokeWithArguments(grouped, instance, pConstructor);

        return RecordCodecBuilder.<T>build(Generic.cast(applied)).codec();
    }

    private static <T> @NotNull Codec<T> getRawCodec(Class<T> pClass) {
        Codec<?> codec = CODECS.get(pClass);

        if (codec != null) {
            return Generic.cast(codec);
        }

        codec = findCodec(pClass);

        if (codec != null) {
            CODECS.put(pClass, codec);
            return Generic.cast(codec);
        }

        if (pClass.isAnnotationPresent(CodecSerializable.class)) {
            return codec(pClass);
        }

        throw new UnsupportedOperationException("Unfounded codec type " + pClass);
    }

    private static <T> Codec<T> findCodec(Class<T> pClass) {
        String name = getCodecField(pClass);

        try {
            Field field = pClass.getField(name);
            field.trySetAccessible();
            Object codec = field.get(null);
            return Generic.cast(codec);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}

        return null;
    }
    
    private static String getFieldName(@NotNull RecordComponent pComponent) {
        if (!pComponent.isAnnotationPresent(SerializeField.class)) {
            return pComponent.getName();
        }

        SerializeField annotation = pComponent.getAnnotation(SerializeField.class);
        return annotation.value().isEmpty() ? pComponent.getName() : annotation.value();
    }

    private static String getFieldName(@NotNull Field pField) {
        if (!pField.isAnnotationPresent(SerializeField.class)) {
            return pField.getName();
        }

        SerializeField annotation = pField.getAnnotation(SerializeField.class);
        return annotation.value().isEmpty() ? pField.getName() : annotation.value();
    }

    private static String getCodecField(@NotNull Class<?> pClass) {
        if (!pClass.isAnnotationPresent(CodecSerializable.class)) {
            return "CODEC";
        }

        return pClass.getAnnotation(CodecSerializable.class).value();
    }

    private static MethodHandle product(
            @NotNull MethodHandles.Lookup pLookup,
            @NotNull Class<?> pClass
    ) throws IllegalAccessException {
        for (Method method : pClass.getDeclaredMethods()) {
            if ("apply".equals(method.getName())) {
                return pLookup.unreflect(method);
            }
        }
        throw new RuntimeException("Unfounded apply method");
    }

    static {
        CODECS = new IdentityHashMap<>();
        CODECS.put(Byte.TYPE, Codec.BYTE);
        CODECS.put(Short.TYPE, Codec.SHORT);
        CODECS.put(Integer.TYPE, Codec.INT);
        CODECS.put(Long.TYPE, Codec.LONG);
        CODECS.put(Float.TYPE, Codec.FLOAT);
        CODECS.put(Double.TYPE, Codec.DOUBLE);
        CODECS.put(Boolean.TYPE, Codec.BOOL);
        CODECS.put(String.class, Codec.STRING);

        METHOD_GROUP = new MethodHandle[16];

        MethodHandles.Lookup pLookup = MethodHandles.lookup();

        for (Method method : Kind1.class.getDeclaredMethods()) {
            if (!"group".equals(method.getName())) {
                continue;
            }

            try {
                MethodHandle handle = pLookup.unreflect(method);
                METHOD_GROUP[method.getParameterCount() - 1] = handle;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        Class<?>[] classes = Products.class.getDeclaredClasses();
        METHOD_APPLY = new MethodHandle[classes.length];
        try {
            for (Class<?> pclass : classes) {
                int count = pclass.getTypeParameters().length - 2;
                METHOD_APPLY[count] = product(pLookup, pclass);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
