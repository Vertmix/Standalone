package com.cjcameron92.games.standalone.listeners;

import com.cjcameron92.games.standalone.api.packet.packets.out.player.PlayerMessagePacket;
import com.google.common.eventbus.Subscribe;

public class PlayerListener {

    @Subscribe
    public void onPlayerMessage(PlayerMessagePacket packet) {
        System.out.println(packet.getProfile().getName());
    }
}
