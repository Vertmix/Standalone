package com.cjcameron92.games.standalone.api.packet.packets.out.player;

import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class PlayerTeleportPacket extends PlayerPacket {

    private final LocationWrapper locationWrapper;

    public PlayerTeleportPacket(PlayerProfile profile, LocationWrapper locationWrapper) {
        super(profile);
        this.locationWrapper = locationWrapper;
    }

    @Getter
    @AllArgsConstructor
    public static class LocationWrapper {

        private final String world;
        private final double x, y, z;
        private final float yaw, pitch;

    }
}
