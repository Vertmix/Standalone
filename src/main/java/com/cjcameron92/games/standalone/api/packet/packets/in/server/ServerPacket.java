package com.cjcameron92.games.standalone.api.packet.packets.in.server;

import com.cjcameron92.games.standalone.api.packet.Packet;
import com.cjcameron92.games.standalone.api.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class ServerPacket extends Packet {

    private final Server server;

}
