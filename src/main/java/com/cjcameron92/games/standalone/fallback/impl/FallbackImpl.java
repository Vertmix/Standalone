package com.cjcameron92.games.standalone.fallback.impl;

import com.cjcameron92.games.standalone.api.server.Server;
import com.cjcameron92.games.standalone.fallback.Fallback;
import com.cjcameron92.games.standalone.fallback.FallbackStrategy;
import com.cjcameron92.games.standalone.api.service.ServerService;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class FallbackImpl implements Fallback {

    private final ServerService service;

    @Override
    public void fallback(Server server, FallbackStrategy strategy) {
        // TODO: 2023-07-22 Implement logic for fallback


    }
}
