package net.quepierts.animata.core.math.transform;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform implements ITransform {
    private final Vector3f rotation;
    private final Vector3f position;
    private final Vector3f scale;

    public Transform() {
        this.rotation = new Vector3f();
        this.position = new Vector3f();
        this.scale = new Vector3f(1.0f);
    }

    public Transform(ITransform transform) {
        this.rotation = transform.getRotation();
        this.position = transform.getPosition();
        this.scale = transform.getScale();
    }

    public Transform(Vector3f rotation, Vector3f position, Vector3f scale) {
        this.rotation = new Vector3f(rotation);
        this.position = new Vector3f(position);
        this.scale = new Vector3f(scale);
    }

    public Transform(
            int xRot, int yRot, int zRot,
            int xPos, int yPos, int zPos,
            int xScl, int yScl, int zScl
    ) {
        this.rotation = new Vector3f(xRot, yRot, zRot);
        this.position = new Vector3f(xPos, yPos, zPos);
        this.scale = new Vector3f(xScl, yScl, zScl);
    }

    @Override
    public void setRotation(float xRot, float yRot, float zRot) {
        this.rotation.set(xRot, yRot, zRot);
    }

    @Override
    public void setPosition(float xPos, float yPos, float zPos) {
        this.position.set(xPos, yPos, zPos);
    }

    @Override
    public void setScale(float xScale, float yScale, float zScale) {
        this.scale.set(xScale, yScale, zScale);
    }

    @Override
    public void setRotationX(float x) {
        this.rotation.x = x;
    }

    @Override
    public void setRotationY(float y) {
        this.rotation.y = y;
    }

    @Override
    public void setRotationZ(float z) {
        this.rotation.z = z;
    }

    @Override
    public void setPositionX(float x) {
        this.position.x = x;
    }

    @Override
    public void setPositionY(float y) {
        this.position.y = y;
    }

    @Override
    public void setPositionZ(float z) {
        this.position.z = z;
    }

    @Override
    public void setScaleX(float x) {
        this.scale.x = x;
    }

    @Override
    public void setScaleY(float y) {
        this.scale.y = y;
    }

    @Override
    public void setScaleZ(float z) {
        this.scale.z = z;
    }

    @Override
    public void getRotation(@NotNull Vector3f pOut) {
        pOut.set(this.rotation);
    }

    @Override
    public void getPosition(@NotNull Vector3f pOut) {
        pOut.set(this.position);
    }

    @Override
    public void getScale(@NotNull Vector3f pOut) {
        pOut.set(this.scale);
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
        matrix4f.translate(this.position.x / 16.0F, this.position.y / 16.0F, this.position.z / 16.0F);
        if (this.rotation.x != 0.0F || this.rotation.y != 0.0F || this.rotation.z != 0.0F) {
            matrix4f.rotateZYX(this.rotation.x, this.rotation.y, this.rotation.z);
        }

        if (this.scale.x != 1.0F || this.scale.y != 1.0F || this.scale.z != 1.0F) {
            matrix4f.scale(this.scale.x, this.scale.y, this.scale.z);
        }
    }

    @Override
    public void getTransform(IPoseAcceptor pPose) {
        pPose.accept(this.position, this.rotation, this.scale);
    }

    @Override
    public void resetPose() {
        this.position.set(0);
        this.rotation.set(0);
        this.scale.set(1);
    }

    public static final class Initialed extends Transform {
        private final Vector3f defRotation;
        private final Vector3f defPosition;
        private final Vector3f defScale;

        public Initialed() {
            this.defRotation = new Vector3f();
            this.defPosition = new Vector3f();
            this.defScale = new Vector3f(1.0f);
        }

        public Initialed(ITransform transform) {
            super(transform);
            this.defRotation = transform.getRotation();
            this.defPosition = transform.getPosition();
            this.defScale = transform.getScale();
        }

        public Initialed(Vector3f rotation, Vector3f position, Vector3f scale) {
            super(rotation, position, scale);
            this.defRotation = new Vector3f(rotation);
            this.defPosition = new Vector3f(position);
            this.defScale = new Vector3f(scale);
        }

        public Initialed(
                int xRot, int yRot, int zRot,
                int xPos, int yPos, int zPos,
                int xScl, int yScl, int zScl
        ) {
            super(xRot, yRot, zRot, xPos, yPos, zPos, xScl, yScl, zScl);
            this.defRotation = new Vector3f(xRot, yRot, zRot);
            this.defPosition = new Vector3f(xPos, yPos, zPos);
            this.defScale = new Vector3f(xScl, yScl, zScl);
        }

        @Override
        public void getInitialRotation(@NotNull Vector3f pOut) {
            pOut.set(this.defRotation);
        }

        @Override
        public void getInitialPosition(@NotNull Vector3f pOut) {
            pOut.set(this.defPosition);
        }

        @Override
        public void getInitialScale(@NotNull Vector3f pOut) {
            pOut.set(this.defScale);
        }

        @Override
        public void resetPose() {
            this.setRotation(this.defRotation);
            this.setPosition(this.defPosition);
            this.setScale(this.defScale);
        }
    }
}
