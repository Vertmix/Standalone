package com.cjcameron92.games.standalone.listeners;


import com.cjcameron92.games.standalone.api.server.Server;
import com.cjcameron92.games.standalone.api.server.ServerStatus;

import com.cjcameron92.games.standalone.fallback.Fallback;
import com.cjcameron92.games.standalone.fallback.FallbackStrategy;
import com.cjcameron92.games.standalone.service.ServerService;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerListener {

    private final ServerService service;
    private final Fallback fallback;

//    @Subscribe
//    public void onServerModifyStatus(PacketServerModifyStatus packet) {
//        final Server server = packet.getServer();
//        // check if the server is offline
//        if (server.getServerStatus() == ServerStatus.OFFLINE) {
//            fallback.fallback(server, FallbackStrategy.MIN_PLAYERS);
//        }
//        // update the cache with the server information
//        service.setServer(server);
//    }
}
