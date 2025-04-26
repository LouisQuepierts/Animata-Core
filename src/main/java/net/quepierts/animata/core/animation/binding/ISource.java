package net.quepierts.animata.core.animation.binding;

import java.util.Arrays;

public interface ISource {
    ISource ONE = new ISource() {
        @Override
        public void eval(float[] pBuffer, float time) {
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

    void eval(float[] pBuffer, float time);

    int getDimension();

    String getName();
}
