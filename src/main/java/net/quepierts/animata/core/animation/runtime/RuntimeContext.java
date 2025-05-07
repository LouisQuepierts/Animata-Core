package net.quepierts.animata.core.animation.runtime;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.quepierts.animata.core.animation.cache.AnimationCache;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import net.quepierts.animata.core.animation.runtime.field.CacheNodeField;
import net.quepierts.animata.core.animation.runtime.field.ConstantField;
import net.quepierts.animata.core.animation.runtime.field.RuntimeField;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RuntimeContext {
    private final AnimationCache delegateCache;
    private final Map<String, RuntimeField> cached;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private float time;

    public void fetch(String pPath, float[] pReceptor) {

    }

    public void assign(String pPath, float[] pValues) {

    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    static class Builder {
        private final AnimationCache delegateCache;

        private final Map<String, RuntimeField> fields = new Object2ObjectOpenHashMap<>();
        private float time;

        public Builder time(float pTime) {
            this.time = pTime;
            return this;
        }

        public Builder node(String pPath, AnimationCacheNode pNode) {
            this.fields.put(pPath, new CacheNodeField(pNode));
            return this;
        }

        public Builder value(String pPath, float[] pValues) {
            this.fields.put(pPath, new ConstantField(pValues));
            return this;
        }

        public RuntimeContext build() {
            return new RuntimeContext(delegateCache, fields);
        }
    }
}
