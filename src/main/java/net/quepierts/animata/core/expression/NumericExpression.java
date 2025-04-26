package net.quepierts.animata.core.expression;

public interface NumericExpression {
    float BOOLEAN_TRUE = 1.0f;
    float BOOLEAN_FALSE = 0.0f;
    float BOOLEAN_THRESHOLD = 0.5f;

    static NumericExpression parse(String string) {
        if (string.isEmpty() || string.isBlank()) {
            return Constant.ZERO;
        }

        switch (string.charAt(0)) {
            case '=':
                throw new UnsupportedOperationException();
            case 'v': case 'V':
                float number = 0.0f;
                try {
                    number = Float.parseFloat(string.substring(1));
                } catch (NumberFormatException ignored) {}
                return new Variable(number);
        }

        return Constant.ZERO;
    }

    float getNumber();

    void setNumber(float v);

    default boolean getBoolean() {
        return this.getNumber() > BOOLEAN_THRESHOLD;
    }

    default void setBoolean(boolean v) {
        this.setNumber(v ? BOOLEAN_TRUE : BOOLEAN_FALSE);
    }

    default boolean mutable() {
        return true;
    }

    default String getString() {
        return String.valueOf(this.getNumber());
    }

}
