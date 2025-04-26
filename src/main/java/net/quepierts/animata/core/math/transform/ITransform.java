package net.quepierts.animata.core.math.transform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public interface ITransform {
    default void setTransform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.setRotation(rotation.x, rotation.y, rotation.z);
        this.setPosition(position.x, position.y, position.z);
        this.setScale(scale.x, scale.y, scale.z);
    }

    default void setRotation(Vector3f rotation) {
        this.setRotation(rotation.x, rotation.y, rotation.z);
    }

    default void setPosition(Vector3f position) {
        this.setPosition(position.x(), position.y(), position.z());
    }

    default void setScale(@NotNull Vector3f scale) {
        this.setScale(scale.x(), scale.y(), scale.z());
    }

    default void setTransform(@NotNull ITransform transform) {
        this.setRotation(transform.getRotation());
        this.setPosition(transform.getPosition());
        this.setScale(transform.getScale());
    }

    default void getTransform(IPoseAcceptor pPose) {
        Vector3f position = this.getPosition();
        Vector3f rotation = this.getRotation();
        Vector3f scale = this.getScale();
        pPose.accept(position, rotation, scale);
    }

    void setRotation(float xRot, float yRot, float zRot);

    void setPosition(float xPos, float yPos, float zPos);

    void setScale(float xScale, float yScale, float zScale);

    void setRotationX(float x);

    void setRotationY(float y);

    void setRotationZ(float z);

    void setPositionX(float x);

    void setPositionY(float y);

    void setPositionZ(float z);

    void setScaleX(float x);

    void setScaleY(float y);

    void setScaleZ(float z);

    @Contract("-> new")
    default Vector3f getRotation() {
        Vector3f out = new Vector3f();
        this.getRotation(out);
        return out;
    }

    @Contract("-> new")
    default Vector3f getPosition() {
        Vector3f out = new Vector3f();
        this.getPosition(out);
        return out;
    }

    @Contract("-> new")
    default Vector3f getScale() {
        Vector3f out = new Vector3f();
        this.getScale(out);
        return out;
    }

    void getRotation(@NotNull Vector3f pOut);

    void getPosition(@NotNull Vector3f pOut);

    void getScale(@NotNull Vector3f pOut);

    void getInitialRotation(@NotNull Vector3f pOut);

    void getInitialPosition(@NotNull Vector3f pOut);

    void getInitialScale(@NotNull Vector3f pOut);

    void getTransform(Matrix4f matrix4f);

    void resetPose();
}
