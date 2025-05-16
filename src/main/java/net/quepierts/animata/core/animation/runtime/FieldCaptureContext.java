package net.quepierts.animata.core.animation.runtime;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class FieldCaptureContext
        implements RuntimeContext, UniformDeclarationProvider {
    private final Map<String, CapturedField> captured = new Object2ObjectOpenHashMap<>();
    private final Predicate isRegistered;

    @Override
    public void fetch(String pPath, float[] pOut) {
        if (this.isRegistered.test(pPath)) {
            return;
        }

        CapturedField capture = this.captured.get(pPath);
        if (capture == null) {
            capture = new CapturedField(pPath, pOut.length);
            this.captured.put(pPath, capture);
        }

        capture.read = true;
    }

    @Override
    public void write(String pPath, float[] pIn) {
        if (this.isRegistered.test(pPath)) {
            return;
        }

        CapturedField capture = this.captured.get(pPath);
        if (capture == null) {
            capture = new CapturedField(pPath, pIn.length);
            this.captured.put(pPath, capture);
        }

        capture.write = true;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    public void setProgress(float pProgress) {

    }

    @Override
    public void getUniforms(@NotNull Collection<UniformDeclaration> pOut) {
        this.captured.values()
                .stream()
                .map(CapturedField::toRequiredField)
                .forEach(pOut::add);
    }

    @FunctionalInterface
    public interface Predicate {
        boolean test(String pPath);
    }

    @RequiredArgsConstructor
    private static class CapturedField {
        private final String path;
        private final int dimension;

        private boolean write;
        private boolean read;

        public UniformDeclaration toRequiredField() {
            UniformDeclaration.Type type = UniformDeclaration.Type.READWRITE;

            // write and read must have one is true
            if (!this.write) {
                type = UniformDeclaration.Type.READ;
            } else if (!this.read) {
                type = UniformDeclaration.Type.WRITE;
            }

            return new UniformDeclaration(path, dimension, new float[dimension], type);
        }
    }
}
