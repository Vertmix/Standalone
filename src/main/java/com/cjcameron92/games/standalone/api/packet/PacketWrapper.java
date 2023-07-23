package com.cjcameron92.games.standalone.api.packet;

import com.cjcameron92.games.standalone.api.TargetType;
import lombok.Getter;

@Getter
public class PacketWrapper {

    private final Packet packet;
    private final String packetType;
    private final TargetType targetType;

    public PacketWrapper(Packet packet, TargetType targetType) {
        this.packet = packet;
        this.packetType = packet.getClass().getName();
        this.targetType = targetType;
    }

}