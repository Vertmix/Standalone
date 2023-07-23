package com.cjcameron92.games.standalone.service;

import com.cjcameron92.games.standalone.api.ClientBootstrap;
import com.cjcameron92.games.standalone.api.redis.Redis;
import com.cjcameron92.games.standalone.api.server.Server;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerService {

    private static final String TABLE_KEY = "server:cache";
    private final Cache<String, Server> profiles = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private final Redis redis;

    public ServerService(Redis redis) {
        this.redis = redis;
    }

    public CompletableFuture<Set<Server>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                return jedis.hgetAll(TABLE_KEY).values().stream().map(s -> ClientBootstrap.getGSON().fromJson(s, Server.class)).collect(Collectors.toSet());
            }
        });
    }

    public CompletableFuture<Server> get(String serverId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return profiles.get(serverId, () -> {
                    try (Jedis jedis = this.redis.getJedis()) {
                        final String string = jedis.hget(TABLE_KEY, serverId);
                        return ClientBootstrap.getGSON().fromJson(string, Server.class);
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public void add(Server server) {
        this.profiles.put(server.getServerId(), server);

        CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                jedis.hset(TABLE_KEY, server.getServerId(), ClientBootstrap.getGSON().toJson(server));
            }
            return null;
        });
    }

    public void invalidate(Server server) {
        this.profiles.invalidate(server.getServerId());;
    }

    public void delete(Server server) {
        invalidate(server);
        CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = this.redis.getJedis()) {
                jedis.hdel(TABLE_KEY, server.getServerId());
            }
            return null;
        });
    }
}
