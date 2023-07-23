package com.cjcameron92.games.standalone.fallback;

import com.cjcameron92.games.standalone.api.server.Server;

public interface Fallback {

    void fallback(Server server, FallbackStrategy strategy);
}
