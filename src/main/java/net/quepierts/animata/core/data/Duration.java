package net.quepierts.animata.core.data;

import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.quepierts.animata.core.datafix.CodecHelper;
import net.quepierts.animata.core.datafix.annotations.CodecSerializable;
import org.jetbrains.annotations.NotNull;

@Getter
@CodecSerializable
public final class Duration implements Comparable<Duration> {
    public static final Codec<Duration> CODEC = Codec.FLOAT.xmap(
            Duration::new,
            Duration::getTick
    );

    public static final Duration ZERO = new Duration(0);

    public static final char IDENTIFIER_HOUR = 'H';
    public static final char IDENTIFIER_MINUTE = 'M';
    public static final char IDENTIFIER_SECOND = 'S';

    private final float tick;

    private Duration(float millis) {
        this.tick = millis;
    }

    public static Duration fromString(String string) {
        char identifier = string.charAt(string.length() - 1);
        String number = Character.isDigit(identifier) ? string : string.substring(0, string.length() - 1);

        float value;
        try {
            value = Float.parseFloat(number);
        } catch (NumberFormatException e) {
//            log.warn("Illegal duration format: ");
            return Duration.ZERO;
        }

        if (identifier > 96) {
            identifier -= 32;
        }

        return switch (identifier) {
            case IDENTIFIER_HOUR -> Duration.hour(value);
            case IDENTIFIER_MINUTE -> Duration.minute(value);
            case IDENTIFIER_SECOND -> Duration.second(value);
            default -> Duration.tick(value);
        };
    }

    public static Duration tick(float ticks) {
        return new Duration(ticks);
    }

    public static Duration second(int second) {
        return new Duration(second * 20);
    }

    public static Duration second(float second) {
        return new Duration(second * 20f);
    }

    public static Duration minute(int minute) {
        return new Duration(minute * 20 * 60);
    }

    public static Duration minute(float minute) {
        return new Duration((int) (minute * 20 * 60));
    }

    public static Duration hour(int hour) {
        return new Duration(hour * 20 * 60 * 60);
    }

    public static Duration hour(float hour) {
        return new Duration((int) (hour * 20 * 60 * 60));
    }

    public static Duration max(Duration a, Duration b) {
        return a.tick > b.tick ? a : b;
    }

    public Duration copy() {
        return new Duration(this.tick);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.tick);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Duration duration) {
            return this.tick == duration.tick;
        } else if (obj instanceof Number number) {
            return this.tick == number.floatValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.tick);
    }

    @Override
    public int compareTo(@NotNull Duration duration) {
        return Float.compare(this.tick, duration.tick);
    }
}
