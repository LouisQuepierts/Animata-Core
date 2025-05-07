package net.quepierts.animata.core.animation.runtime.field;

import net.quepierts.animata.core.property.Readable;
import net.quepierts.animata.core.property.Writable;

public interface RuntimeField extends Readable, Writable {
    int getDimension();
}
