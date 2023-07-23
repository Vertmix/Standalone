package com.cjcameron92.games.standalone.api.packet;

import com.google.common.eventbus.EventBus;

public class PacketService {

    private final EventBus eventBus;

    public PacketService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void registerPacketListener(PacketListener listener) {
        eventBus.register(listener);
    }

    public void unregisterPacketListener(PacketListener listener) {
        eventBus.unregister(listener);
    }
}
