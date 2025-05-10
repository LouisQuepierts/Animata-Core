package net.quepierts.animata.core.animation.runtime;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.property.Property;
import net.quepierts.animata.core.animation.runtime.field.CacheNodeField;
import net.quepierts.animata.core.animation.runtime.field.ConstantField;
import net.quepierts.animata.core.animation.runtime.field.RuntimeField;
import net.quepierts.animata.platform.services.IRuntimePlatformDetector;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CachedRuntimeContext implements RuntimeContext {
    private final AnimationCache cache;
    private final Map<String, RuntimeField> fields;

    @Getter
    private final String domainName;

    @Getter
    @Setter
    private float progress;

    void increaseTime(float pDelta) {
        this.progress += pDelta;
    }

    @Override
    public void fetch(String pPath, float[] pOut) {
        this.getField(pPath, pOut.length).fetch(pOut);
    }

    @Override
    public void write(String pPath, float[] pIn) {
        this.getField(pPath, pIn.length).write(pIn);
    }

    private RuntimeField getField(String pPath, int dimension) {
        RuntimeField field = this.fields.get(pPath);

        if (field != null) {
            return field;
        }

        if (!IRuntimePlatformDetector.INSTANCE.isDevelopmentEnvironment()) {
            throw new RuntimeException("No field found for " + pPath);
        }

        Property property = this.cache.getTransientProperty(this.domainName, pPath);

        if (property == null) {
            property = this.cache.getCacheProperty(pPath);
        }

        if (property != null && property.getDimension() >= dimension) {
            RuntimeField wrapper = new CacheNodeField(property);
            this.fields.put(pPath, wrapper);
            return wrapper;
        } else {
            RuntimeField wrapper = new ConstantField(new float[dimension]);
            this.fields.put(pPath, wrapper);
            return wrapper;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    static class Builder {
        private final AnimationCache delegateCache;
        private final String domainName;

        private final Map<String, RuntimeField> fields = new Object2ObjectOpenHashMap<>();
        private float time;

        public Builder time(float pTime) {
            this.time = pTime;
            return this;
        }

        public Builder node(String pPath, Property pProperty) {
            this.fields.put(pPath, new CacheNodeField(pProperty));
            return this;
        }

        public Builder value(String pPath, float[] pValues) {
            this.fields.put(pPath, new ConstantField(pValues));
            return this;
        }

        public boolean registered(String pPath) {
            return this.fields.containsKey(pPath);
        }

        public CachedRuntimeContext build() {
            CachedRuntimeContext context = new CachedRuntimeContext(delegateCache, fields, domainName);
            context.setProgress(time);
            return context;
        }
    }
}
