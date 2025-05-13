package net.quepierts.animata.core.property;

import net.quepierts.animata.core.math.transform.Transformable;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TransformProperty implements Transformable {

    @AnimataProperty
    private final Vector3fProperty rotation;

    @AnimataProperty
    private final Vector3fProperty position;

    @AnimataProperty
    private final Vector3fProperty scale;

    public TransformProperty() {
        this.rotation = new Vector3fProperty("rotation");
        this.position = new Vector3fProperty("position");
        this.scale = new Vector3fProperty("scale", 1);
    }

    @Override
    public void setRotation(float xRot, float yRot, float zRot) {
        this.rotation.getCache().set(xRot, yRot, zRot);
    }

    @Override
    public void setPosition(float xPos, float yPos, float zPos) {
        this.position.getCache().set(xPos, yPos, zPos);
    }

    @Override
    public void setScale(float xScale, float yScale, float zScale) {
        this.scale.getCache().set(xScale, yScale, zScale);
    }

    @Override
    public void setRotationX(float x) {
        this.rotation.getCache().x = x;
    }

    @Override
    public void setRotationY(float y) {
        this.rotation.getCache().y = y;
    }

    @Override
    public void setRotationZ(float z) {
        this.rotation.getCache().z = z;
    }

    @Override
    public void setPositionX(float x) {
        this.position.getCache().x = x;
    }

    @Override
    public void setPositionY(float y) {
        this.position.getCache().y = y;
    }

    @Override
    public void setPositionZ(float z) {
        this.position.getCache().z = z;
    }

    @Override
    public void setScaleX(float x) {
        this.scale.getCache().x = x;
    }

    @Override
    public void setScaleY(float y) {
        this.scale.getCache().y = y;
    }

    @Override
    public void setScaleZ(float z) {
        this.scale.getCache().z = z;
    }

    @Override
    public void getRotation(@NotNull Vector3f pOut) {
        pOut.set(this.rotation.getCache());
    }

    @Override
    public void getPosition(@NotNull Vector3f pOut) {
        pOut.set(this.position.getCache());
    }

    @Override
    public void getScale(@NotNull Vector3f pOut) {
        pOut.set(this.scale.getCache());
    }

    @Override
    public void getInitialRotation(@NotNull Vector3f pOut) {
        pOut.set(0);
    }

    @Override
    public void getInitialPosition(@NotNull Vector3f pOut) {
        pOut.set(0);
    }

    @Override
    public void getInitialScale(@NotNull Vector3f pOut) {
        pOut.set(1);
    }

    @Override
    public void getTransform(Matrix4f matrix4f) {
        matrix4f.translate(this.position.getCache());
        if (this.rotation.getCache().x != 0.0F || this.rotation.getCache().y != 0.0F || this.rotation.getCache().z != 0.0F) {
            matrix4f.rotateZYX(this.rotation.getCache());
        }
        if (this.scale.getCache().x != 1.0F || this.scale.getCache().y != 1.0F || this.scale.getCache().z != 1.0F) {
            matrix4f.scale(this.scale.getCache());
        }
    }

    @Override
    public void resetPose() {
        this.rotation.getCache().set(0);
        this.position.getCache().set(0);
        this.scale.getCache().set(1);
    }
}
