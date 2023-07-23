package com.cjcameron92.games.standalone.api.packet.packets.out.player;

import com.cjcameron92.games.standalone.api.packet.Packet;
import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerPacket extends Packet {

    private final PlayerProfile profile;
}
