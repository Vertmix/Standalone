package com.cjcameron92.games.standalone.api.packet.packets.out.player;

import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import lombok.Getter;

@Getter
public class PlayerMessagePacket extends PlayerPacket {

    private final String message;

    public PlayerMessagePacket(PlayerProfile profile, String message) {
        super(profile);
        this.message = message;
    }
}
