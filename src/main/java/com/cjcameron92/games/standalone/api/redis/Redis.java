package com.cjcameron92.games.standalone.api.redis;

import com.cjcameron92.games.standalone.api.TargetType;
import com.cjcameron92.games.standalone.api.packet.PacketDeserializer;
import com.cjcameron92.games.standalone.api.packet.PacketListener;
import com.cjcameron92.games.standalone.api.packet.PacketSerializer;
import com.cjcameron92.games.standalone.api.packet.PacketWrapper;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Redis {

    private static final PacketDeserializer DESERIALIZER = new PacketDeserializer();
    private static final PacketSerializer SERIALIZER = new PacketSerializer();

    private final JedisPool jedisPool;
    private final PacketListener packetListener;

    private final TargetType targetType;

    private PubSubListener listener = null;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public Redis(RedisCredentials credentials, PacketListener packetListener, TargetType targetType) {
        this.targetType = targetType;
        this.packetListener = packetListener;
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(16);

        if (credentials.password().trim().isEmpty()) {
            this.jedisPool = new JedisPool(config, credentials.address(), credentials.port());
        } else {
            this.jedisPool = new JedisPool(config, credentials.address(), credentials.port(), 2000, credentials.password());
        }

        try (Jedis jedis = this.jedisPool.getResource()) {
            Logger.info(jedis.ping());
        }
        new Thread(new Runnable() {
            private boolean broken = false;

            @Override
            public void run() {
                if (this.broken) {
                    Logger.info("[Standalone] Trying subscription...");
                    this.broken = false;
                }

                try (Jedis jedis = getJedis()) {
                    try {
                        listener = new PubSubListener();
                        jedis.subscribe(listener, "game-packet".getBytes(StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        new RuntimeException("Error subscribing to listener", e).printStackTrace();
                        try {
                            listener.unsubscribe();
                        } catch (Exception ignored) {}

                        listener = null;
                        this.broken = true;
                    }
                }

                if (broken) {
                    executorService.schedule(this, 100, TimeUnit.MILLISECONDS);
                }

            }
        }, "Redis Thread").start();

    }


    public void publish(PacketWrapper packetWrapper) {
        executorService.submit(() -> {
           final String serialized = SERIALIZER.serialize(packetWrapper);
           try (Jedis jedis = getJedis()) {
               jedis.publish("game-packet".getBytes(StandardCharsets.UTF_8), serialized.getBytes(StandardCharsets.UTF_8));
           }
        });
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public void close() {
        this.jedisPool.close();
    }

    @Getter
    public final class PubSubListener extends BinaryJedisPubSub {
        private final ReentrantLock lock = new ReentrantLock();

        @Getter
        private final Set<String> subscribed = ConcurrentHashMap.newKeySet();

        @Override
        public void subscribe(byte[]... channels) {
            this.lock.lock();
            try {
                for (byte[] channel : channels) {
                    final String channelName = new String(channel, StandardCharsets.UTF_8);
                    if (this.subscribed.add(channelName)) {
                        super.subscribe(channel);
                    }
                }
            } finally {
                this.lock.unlock();
            }
        }

        @Override
        public void unsubscribe(byte[]... channels) {
            this.lock.lock();
            try {
                super.unsubscribe(channels);
            } finally {
                this.lock.unlock();
            }
        }

        @Override
        public void onSubscribe(byte[] channel, int subscribedChannels) {
            Logger.info("[Standalone] Subscribed to channel: " + new String(channel, StandardCharsets.UTF_8));
        }

        @Override
        public void onUnsubscribe(byte[] channel, int subscribedChannels) {
            String channelName = new String(channel, StandardCharsets.UTF_8);
            Logger.info("[Standalone] Unsubscribed from channel: " + channelName);
            this.subscribed.remove(channelName);
        }

        @Override
        public void onMessage(byte[] channel, byte[] message) {
            final String string = new String(message, StandardCharsets.UTF_8);
            final String channelName = new String(channel, StandardCharsets.UTF_8);
            if (channelName.equalsIgnoreCase("game-packet")) {
                final PacketWrapper packetWrapper = DESERIALIZER.deserialize(string);
                if (packetWrapper != null) {
                    if (packetWrapper.getTargetType() == TargetType.ALL || targetType == packetWrapper.getTargetType()) {
                        packetListener.onPacketReceived(packetWrapper);
                    }
                }
            }
        }
    }
}