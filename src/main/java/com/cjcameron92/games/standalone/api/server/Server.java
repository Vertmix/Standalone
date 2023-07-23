package com.cjcameron92.games.standalone.api.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Server {

    @NonNull
    private final String serverId;
    @NonNull
    private final ServerType serverType;

    @Setter
    private transient ServerStatus serverStatus = ServerStatus.OFFLINE;

    @Setter
    private transient int online = 0;
}
