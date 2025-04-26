package net.quepierts.animata.core.expression;

public class Constant implements NumericExpression {
    public static final NumericExpression ZERO = new Constant();

    private final float value;

    public Constant() {
        this.value = 0.0f;
    }

    public Constant(float value) {
        this.value = value;
    }

    @Override
    public float getNumber() {
        return this.value;
    }

    @Override
    public void setNumber(float v) {

    }

    @Override
    public boolean mutable() {
        return false;
    }
}
