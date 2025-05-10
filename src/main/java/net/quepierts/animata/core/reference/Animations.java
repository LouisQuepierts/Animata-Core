package net.quepierts.animata.core.reference;

import net.quepierts.animata.core.animation.AnimationSequence;
import net.quepierts.animata.core.animation.timeline.ImmutableTimelineAnimationSequence;
import net.quepierts.animata.core.animation.timeline.ImmutableTrack;
import net.quepierts.animata.core.animation.timeline.VectorKeyFrame;
import net.quepierts.animata.core.data.ConstantVector;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.Duration;

public class Animations {
    public static final AnimationSequence TIMELINE = ImmutableTimelineAnimationSequence.of(
            ImmutableTrack.of(
                    "root.body",
                    DataType.FLOAT3,
                    VectorKeyFrame.linear(Duration.ZERO, ConstantVector.vector3(0f, 0f, 0f)),
                    VectorKeyFrame.linear(Duration.second(1), ConstantVector.vector3(0f, 6f, 0f)),
                    VectorKeyFrame.linear(Duration.second(2), ConstantVector.vector3(0f, 0f, 0f))
            ),
            ImmutableTrack.of(
                    "root.body.pivot",
                    DataType.FLOAT3,
                    VectorKeyFrame.linear(Duration.ZERO, ConstantVector.vector3(0f, 0f, 0f)),
                    VectorKeyFrame.linear(Duration.second(2), ConstantVector.vector3(0f, -12f, 0f)),
                    VectorKeyFrame.linear(Duration.second(4), ConstantVector.vector3(0f, 0f, 0f))
            ),
            ImmutableTrack.of(
                    "skeleton::root.body.rotation.x",
                    DataType.FLOAT,
                    VectorKeyFrame.linear(Duration.ZERO, ConstantVector.angle(0f)),
                    VectorKeyFrame.linear(Duration.second(2), ConstantVector.angle(45f)),
                    VectorKeyFrame.linear(Duration.second(4), ConstantVector.angle(0f))
            )
    );
}
