package com.cjcameron92.games.standalone.api.packet;

import com.cjcameron92.games.standalone.api.ClientBootstrap;
import com.cjcameron92.games.standalone.api.DataSerializer;

public class PacketSerializer implements DataSerializer<PacketWrapper> {

    @Override
    public String serialize(PacketWrapper type) {
        return ClientBootstrap.getGSON().
                toJsonTree(type, PacketWrapper.class).getAsJsonObject().toString();
    }
}
