package net.quepierts.animata.core.expression;

public class Variable implements NumericExpression {
    private float value;

    public Variable() {
        this.value = 0.0f;
    }

    public Variable(float value) {
        this.value = value;
    }

    @Override
    public float getNumber() {
        return this.value;
    }

    @Override
    public void setNumber(float v) {
        this.value = v;
    }

    @Override
    public String getString() {
        return "v" + this.value;
    }
}
