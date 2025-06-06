package net.quepierts.animata.core.property;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VectorProperty implements Property {
    private static final String[] COMPONENTS = new String[] { "x", "y", "z" };
    private static final Pattern CHILDREN_PATTERN = Pattern.compile("^children\\[(\\d+)]$");

    @Getter private final String name;
    @Getter private final float[] cache;
    private final Component[] components;

    public VectorProperty(String pName, int pLength, float pValue) {
        this.name = pName;
        this.cache = new float[pLength];
        this.components = new Component[pLength];

        for (int i = 0; i < pLength; i++) {
            String childName = i < 3 ? COMPONENTS[i] : Integer.toString(i);
            this.components[i] = new Component(childName, this, i);
            this.cache[i] = pValue;
        }
    }

    public VectorProperty(String name, int length) {
        this(name, length, 0);
    }

    public void write(float[] pValue) {
        System.arraycopy(pValue, 0, this.cache, 0, Math.min(pValue.length, this.cache.length));
    }

    @Override
    public Property getChild(String pChildName) {
        if (pChildName.length() == 1) {
            return switch (pChildName) {
                case "x" -> this.components[0];
                case "y" -> this.components[1];
                case "z" -> this.components[2];
                default -> null;
            };
        }

        Matcher matcher = CHILDREN_PATTERN.matcher(pChildName);
        if (matcher.matches()) {
            int index = Integer.parseInt(matcher.group(1));
            if (index < this.components.length) {
                return this.components[index];
            }
        }
        return null;
    }

    @Override
    public int getDimension() {
        return this.cache.length;
    }

    @Override
    public void fetch(float[] pOut) {
        System.arraycopy(this.cache, 0, pOut, 0, Math.min(this.cache.length, pOut.length));
    }

    private static class Component extends AbstractProperty {
        private final VectorProperty parent;
        private final int index;

        private Component(String name, VectorProperty parent, int index) {
            super(name);
            this.parent = parent;
            this.index = index;
        }

        @Override
        public void write(float[] pValue) {
            this.parent.cache[this.index] = pValue[0];
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void fetch(float[] pOut) {
            pOut[0] = this.parent.cache[this.index];
        }
    }
}
