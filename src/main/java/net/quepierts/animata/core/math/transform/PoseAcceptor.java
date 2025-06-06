package net.quepierts.animata.core.math.transform;

import org.joml.Vector3f;

@FunctionalInterface
public interface PoseAcceptor {
    void accept(Vector3f pPosition, Vector3f pRotation, Vector3f pScale);
}
