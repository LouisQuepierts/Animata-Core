package net.quepierts.animata.core.animation.binding;

import net.quepierts.animata.core.animation.runtime.RuntimeContext;

import java.util.Arrays;

public interface Source {
    Source ONE = new Source() {
        @Override
        public void eval(float[] pBuffer, RuntimeContext pContext) {
            Arrays.fill(pBuffer, 1f);
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public String getName() {
            return "ONE";
        }
    };

    void eval(float[] pBuffer, RuntimeContext time);

    int getDimension();

    String getName();
}
