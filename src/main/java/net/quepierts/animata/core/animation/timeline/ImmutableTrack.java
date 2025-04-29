package net.quepierts.animata.core.animation.timeline;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.quepierts.animata.core.data.DataType;
import net.quepierts.animata.core.data.Duration;
import net.quepierts.animata.core.util.collection.ImmutableFloatTreeMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableTrack implements Track {
    private final ImmutableFloatTreeMap<VectorKeyFrame> keyframe;

    private final DataType type;
    @Getter private final String name;

    public static ImmutableTrack of(String name, DataType type, VectorKeyFrame... keyframes) {
        for (int i = 0; i < keyframes.length; i++) {
            VectorKeyFrame keyframe = keyframes[i];

            if (!keyframe.interpolator().check(type)) {
                throw new RuntimeException("Invalid DataType for Interpolator");
            }
            keyframes[i] = VectorKeyFrame.cast(keyframe, type);
        }

        ImmutableFloatTreeMap<VectorKeyFrame> map = ImmutableFloatTreeMap.of(keyframes);
        return new ImmutableTrack(map, type, name);
    }

    @Override
    public int getLowerIndex(float pTime) {
        return this.keyframe.getLowerIndex(pTime);
    }

    @Override
    public int getUpperIndex(float pTime) {
        return this.keyframe.getUpperIndex(pTime);
    }

    @Override
    public VectorKeyFrame getLowerEntry(float pTime) {
        return this.keyframe.getLowerEntry(pTime);
    }

    @Override
    public VectorKeyFrame getUpperEntry(float pTime) {
        return this.keyframe.getUpperEntry(pTime);
    }

    @Override
    public VectorKeyFrame get(int pIndex) {
        return this.keyframe.get(pIndex);
    }

    @Override
    public int getDimension() {
        return this.type.getLength();
    }

    @Override
    public Duration getLength() {
        return this.keyframe.getSize() == 0 ? Duration.ZERO : this.keyframe.last().duration();
    }
}
