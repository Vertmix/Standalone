package com.cjcameron92.games.standalone.api;

import com.cjcameron92.games.standalone.api.packet.Packet;
import com.cjcameron92.games.standalone.api.packet.PacketProcessor;
import com.cjcameron92.games.standalone.api.packet.PacketWrapper;
import com.cjcameron92.games.standalone.api.packet.packets.out.player.PlayerMessagePacket;
import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import com.cjcameron92.games.standalone.api.redis.Redis;
import com.cjcameron92.games.standalone.api.redis.RedisCredentials;
import com.cjcameron92.games.standalone.api.server.Server;
import com.cjcameron92.games.standalone.api.service.PlayerService;
import com.cjcameron92.games.standalone.api.service.ServerService;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
public class ClientBootstrap {

    @Getter
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final Redis redis;

    private final EventBus eventBus = new EventBus();
    private final PacketProcessor packetProcessor = new PacketProcessor(eventBus);

    private final ServerService serverService;
    private final PlayerService playerService;

    public ClientBootstrap(RedisCredentials redisCredentials) {
        this(redisCredentials, TargetType.GAME);
    }
    public ClientBootstrap(RedisCredentials credentials, TargetType targetType) {
        this.redis = new Redis(credentials, packetProcessor, targetType);
        this.serverService = new ServerService(redis);
        this.playerService = new PlayerService(redis);
    }

    public void message(@NonNull UUID uniqueId, @NonNull String name, @NonNull String message) {
        final PlayerProfile playerProfile = new PlayerProfile(uniqueId, name);
        sendPacket(new PlayerMessagePacket(playerProfile, message), TargetType.ALL);
    }

    public void teleport(@NonNull UUID uniqueId, @NonNull String name, @NonNull Server server, @NonNull String reason) {

    }

    public void sendPacket(Packet packet, TargetType targetType) {
        this.redis.publish(new PacketWrapper(packet, targetType));
    }

    public void registerListener(Object object) {
        this.eventBus.register(object);
    }
}
