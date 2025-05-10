package net.quepierts.animata.core.property;

public class DummyProperty extends AbstractProperty {
    public static final DummyProperty INSTANCE = new DummyProperty();

    DummyProperty() {
        super("dummy");
    }

    @Override
    public void fetch(float[] pOut) {

    }

    @Override
    public void write(float[] pValue) {

    }
}
