package com.cjcameron92.games.standalone.api.packet;

import com.google.common.eventbus.EventBus;

public class PacketProcessor implements PacketListener {

    private final EventBus eventBus;

    public PacketProcessor(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onPacketReceived(PacketWrapper packetWrapper) {
        eventBus.post(packetWrapper.getPacket());

    }
}
