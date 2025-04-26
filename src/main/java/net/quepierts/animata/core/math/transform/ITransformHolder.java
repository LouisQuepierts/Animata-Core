package net.quepierts.animata.core.math.transform;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface ITransformHolder extends ITransform {
    @NotNull
    ITransform transform();

    @Override
    default void setRotation(float xRot, float yRot, float zRot) {
        this.transform().setRotation(xRot, yRot, zRot);
    }

    @Override
    default void setPosition(float xPos, float yPos, float zPos) {
        this.transform().setPosition(xPos, yPos, zPos);
    }

    @Override
    default void setScale(float xScale, float yScale, float zScale) {
        this.transform().setScale(xScale, yScale, zScale);
    }

    @Override
    default void setRotationX(float x) {
        this.transform().setRotationX(x);
    }

    @Override
    default void setRotationY(float y) {
        this.transform().setRotationY(y);
    }

    @Override
    default void setRotationZ(float z) {
        this.transform().setRotationZ(z);
    }

    @Override
    default void setPositionX(float x) {
        this.transform().setPositionX(x);
    }

    @Override
    default void setPositionY(float y) {
        this.transform().setPositionY(y);
    }

    @Override
    default void setPositionZ(float z) {
        this.transform().setPositionZ(z);
    }

    @Override
    default void setScaleX(float x) {
        this.transform().setScaleX(x);
    }

    @Override
    default void setScaleY(float y) {
        this.transform().setScaleY(y);
    }

    @Override
    default void setScaleZ(float z) {
        this.transform().setScaleZ(z);
    }

    @Override
    default Vector3f getRotation() {
        return this.transform().getRotation();
    }

    @Override
    default Vector3f getPosition() {
        return this.transform().getPosition();
    }

    @Override
    default void getRotation(@NotNull Vector3f pOut) {
        this.transform().getRotation(pOut);
    }

    @Override
    default void getPosition(@NotNull Vector3f pOut) {
        this.transform().getPosition(pOut);
    }

    @Override
    default void getScale(@NotNull Vector3f pOut) {
        this.transform().getScale(pOut);
    }

    @Override
    default Vector3f getScale() {
        return this.transform().getScale();
    }

    @Override
    default void getTransform(Matrix4f matrix4f) {
        this.transform().getTransform(matrix4f);
    }

    @Override
    default void resetPose() {
        this.transform().resetPose();
    }

    @Override
    default void getInitialScale(@NotNull Vector3f pOut) {
        this.transform().getInitialScale(pOut);
    }

    @Override
    default void getInitialPosition(@NotNull Vector3f pOut) {
        this.transform().getInitialPosition(pOut);
    }

    @Override
    default void getInitialRotation(@NotNull Vector3f pOut) {
        this.transform().getInitialRotation(pOut);
    }
}
