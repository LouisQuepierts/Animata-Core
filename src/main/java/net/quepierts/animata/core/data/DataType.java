package net.quepierts.animata.core.data;

import lombok.Getter;

@Getter
public enum DataType {
    FLOAT(1),
    BOOL(1),
    INT(1),
    FLOAT2(2),
    FLOAT3(3),
    FLOAT4(4);

    private final int length;

    DataType(int length) {
        this.length = length;
    }
}
