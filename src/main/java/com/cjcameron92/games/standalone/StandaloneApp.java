package com.cjcameron92.games.standalone;

import com.cjcameron92.games.standalone.api.ClientBootstrap;
import com.cjcameron92.games.standalone.api.TargetType;
import com.cjcameron92.games.standalone.api.packet.PacketProcessor;
import com.cjcameron92.games.standalone.api.packet.PacketWrapper;
import com.cjcameron92.games.standalone.api.packet.packets.out.player.PlayerMessagePacket;
import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import com.cjcameron92.games.standalone.api.redis.Redis;
import com.cjcameron92.games.standalone.api.redis.RedisCredentials;
import com.cjcameron92.games.standalone.fallback.impl.FallbackImpl;
import com.cjcameron92.games.standalone.listeners.PlayerListener;
import com.cjcameron92.games.standalone.listeners.ServerListener;
import com.cjcameron92.games.standalone.service.PlayerService;
import com.cjcameron92.games.standalone.service.ServerService;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


public class StandaloneApp {

    private final ClientBootstrap bootstrap;

    public StandaloneApp() {
        this.bootstrap = new ClientBootstrap(new RedisCredentials("127.0.0.1", 6379, ""), TargetType.STANDALONE);
        bootstrap.registerListener(new PlayerListener());

        final PlayerProfile profile = new PlayerProfile(UUID.randomUUID(), "Alex");
        profile.setLastServer("factions-srv2");

        final PlayerService service = new PlayerService(bootstrap.getRedis());
        service.add(profile);

        // close redis connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> bootstrap.getRedis().close()));
    }


    public static void main(String[] args) {
        new StandaloneApp();

    }
}
