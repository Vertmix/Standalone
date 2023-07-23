package com.cjcameron92.games.standalone.api.packet.packets.in.server;

import com.cjcameron92.games.standalone.api.server.Server;

public class ServerModifyPacket extends ServerPacket {
    public ServerModifyPacket(Server server) {
        super(server);
    }
}
