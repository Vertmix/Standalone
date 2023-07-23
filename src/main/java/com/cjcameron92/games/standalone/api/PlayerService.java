package com.cjcameron92.games.standalone.api.service;

import com.cjcameron92.games.standalone.api.ClientBootstrap;
import com.cjcameron92.games.standalone.api.player.PlayerProfile;
import com.cjcameron92.games.standalone.api.redis.Redis;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerService {

    private static final String TABLE_KEY = "player:cache";
    private final Cache<UUID, PlayerProfile> profiles = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    private final Redis redis;

    public PlayerService(Redis redis) {
        this.redis = redis;
    }

    public CompletableFuture<Set<PlayerProfile>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                return new HashSet<>(jedis.hgetAll(TABLE_KEY).values().stream().map(s -> ClientBootstrap.getGSON().fromJson(s, PlayerProfile.class)).collect(Collectors.toSet()));
            }
        });
    }

    public CompletableFuture<PlayerProfile> get(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return profiles.get(uuid, () -> {
                    try (Jedis jedis = this.redis.getJedis()) {
                        final String string = jedis.hget(TABLE_KEY, uuid.toString());
                        return ClientBootstrap.getGSON().fromJson(string, PlayerProfile.class);
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public void add(PlayerProfile playerProfile) {
        this.profiles.put(playerProfile.getUniqueId(), playerProfile);

        CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                jedis.hset(TABLE_KEY, playerProfile.getUniqueId().toString(), ClientBootstrap.getGSON().toJson(playerProfile));
            }
            return null;
        });
    }

    public void invalidate(PlayerProfile playerProfile) {
        this.profiles.invalidate(playerProfile.getUniqueId());;
    }

    public void delete(PlayerProfile playerProfile) {
        invalidate(playerProfile);
        CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                jedis.hdel(TABLE_KEY, playerProfile.getUniqueId().toString());
            }
            return null;
        });
    }
}

