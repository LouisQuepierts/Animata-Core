package net.quepierts.animata.core.animation.runtime;

public record RequiredField(
        String name,
        int dimension,
        float[] defaultValue,
        Type type
) {

    public enum Type {
        READ,
        WRITE,
        READWRITE;

        public boolean isRead() {
            return this != WRITE;
        }

        public boolean isWrite() {
            return this == READ;
        }
    }
}
