package net.quepierts.animata.core.math.transform;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface TransformHolder extends Transformable {
    @NotNull
    Transformable getTransform();

    @Override
    default void setRotation(float xRot, float yRot, float zRot) {
        this.getTransform().setRotation(xRot, yRot, zRot);
    }

    @Override
    default void setPosition(float xPos, float yPos, float zPos) {
        this.getTransform().setPosition(xPos, yPos, zPos);
    }

    @Override
    default void setScale(float xScale, float yScale, float zScale) {
        this.getTransform().setScale(xScale, yScale, zScale);
    }

    @Override
    default void setRotationX(float x) {
        this.getTransform().setRotationX(x);
    }

    @Override
    default void setRotationY(float y) {
        this.getTransform().setRotationY(y);
    }

    @Override
    default void setRotationZ(float z) {
        this.getTransform().setRotationZ(z);
    }

    @Override
    default void setPositionX(float x) {
        this.getTransform().setPositionX(x);
    }

    @Override
    default void setPositionY(float y) {
        this.getTransform().setPositionY(y);
    }

    @Override
    default void setPositionZ(float z) {
        this.getTransform().setPositionZ(z);
    }

    @Override
    default void setScaleX(float x) {
        this.getTransform().setScaleX(x);
    }

    @Override
    default void setScaleY(float y) {
        this.getTransform().setScaleY(y);
    }

    @Override
    default void setScaleZ(float z) {
        this.getTransform().setScaleZ(z);
    }

    @Override
    default Vector3f getRotation() {
        return this.getTransform().getRotation();
    }

    @Override
    default Vector3f getPosition() {
        return this.getTransform().getPosition();
    }

    @Override
    default void getRotation(@NotNull Vector3f pOut) {
        this.getTransform().getRotation(pOut);
    }

    @Override
    default void getPosition(@NotNull Vector3f pOut) {
        this.getTransform().getPosition(pOut);
    }

    @Override
    default void getScale(@NotNull Vector3f pOut) {
        this.getTransform().getScale(pOut);
    }

    @Override
    default Vector3f getScale() {
        return this.getTransform().getScale();
    }

    @Override
    default void getTransform(Matrix4f matrix4f) {
        this.getTransform().getTransform(matrix4f);
    }

    @Override
    default void resetPose() {
        this.getTransform().resetPose();
    }

    @Override
    default void getInitialScale(@NotNull Vector3f pOut) {
        this.getTransform().getInitialScale(pOut);
    }

    @Override
    default void getInitialPosition(@NotNull Vector3f pOut) {
        this.getTransform().getInitialPosition(pOut);
    }

    @Override
    default void getInitialRotation(@NotNull Vector3f pOut) {
        this.getTransform().getInitialRotation(pOut);
    }
}
