package com.cjcameron92.games.standalone.api.player;

import lombok.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class PlayerProfile {

    @NonNull
    private final UUID uniqueId;

    @Setter
    @NonNull
    private String name;

    @Setter
    private long createdAt;

    private final Map<String, Object> metadata = new ConcurrentHashMap<>();

    @Setter
    private String lastServer;

    @Setter
    private transient String currentServer;




}
