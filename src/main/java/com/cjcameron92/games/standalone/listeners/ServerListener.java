package com.cjcameron92.games.standalone.listeners;


import com.cjcameron92.games.standalone.fallback.Fallback;
import com.cjcameron92.games.standalone.api.service.ServerService;
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
