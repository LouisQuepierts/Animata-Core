package net.quepierts.animata.core.animation.cache.node;

import lombok.Getter;
import net.quepierts.animata.core.animation.cache.AnimationCacheNode;
import org.joml.Vector3f;

public class Vector3fNode extends AbstractAnimationCacheNode {
    @Getter private final Vector3f cache;
    private final Component[] components;

    public Vector3fNode(String name) {
        this(name, new Vector3f());
    }

    public Vector3fNode(String name, float value) {
        this(name, new Vector3f(value));
    }

    public Vector3fNode(String name, Vector3f value) {
        super(name);
        this.cache = value;
        this.components = new Component[]{
                new Component('x', this.cache),
                new Component('y', this.cache),
                new Component('z', this.cache)
        };
    }

    @Override
    public void apply(float[] pValue) {
        this.cache.set(pValue[0], pValue[1], pValue[2]);
    }

    @Override
    public AnimationCacheNode getChild(String pChildName) {
        if (pChildName.length() == 1) {
            char comp = pChildName.charAt(0);
            return switch (comp) {
                case 'x' -> this.components[0];
                case 'y' -> this.components[1];
                case 'z' -> this.components[2];
                default -> null;
            };
        }

        return null;
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public void get(float[] pOut) {
        pOut[0] = this.cache.x;
        pOut[1] = this.cache.y;
        pOut[2] = this.cache.z;
    }

    private static class Component extends AbstractAnimationCacheNode {
        private final Vector3f bound;
        private final char component;

        public Component(char component, Vector3f bound) {
            super(String.valueOf(component));
            this.bound = bound;
            this.component = component;
        }

        @Override
        public void apply(float[] pValue) {
            switch (this.component) {
                case 'x' -> this.bound.x = pValue[0];
                case 'y' -> this.bound.y = pValue[0];
                case 'z' -> this.bound.z = pValue[0];
            }
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void get(float[] pOut) {
            switch (this.component) {
                case 'x' -> pOut[0] = this.bound.x;
                case 'y' -> pOut[0] = this.bound.y;
                case 'z' -> pOut[0] = this.bound.z;
            }
        }
    }
}
