package com.cjcameron92.games.standalone.fallback.impl;

import com.cjcameron92.games.standalone.api.server.Server;
import com.cjcameron92.games.standalone.fallback.Fallback;
import com.cjcameron92.games.standalone.fallback.FallbackStrategy;
import com.cjcameron92.games.standalone.service.ServerService;
import lombok.AllArgsConstructor;
import org.pmw.tinylog.Logger;

import java.util.Comparator;
import java.util.Optional;


@AllArgsConstructor
public class FallbackImpl implements Fallback {

    private final ServerService service;

    @Override
    public void fallback(Server server, FallbackStrategy strategy) {
        // TODO: 2023-07-22 Implement logic for fallback

        if (strategy == FallbackStrategy.MIN_PLAYERS) {
            final Optional<Server> optionalServer = service.getServers().stream().sorted(Comparator.comparingInt(Server::getOnline)).findFirst();
            if (optionalServer.isPresent()) {
                final Server targetServer = optionalServer.get();


            } else {
                Logger.error("Could not find a fallback server for fallback strategy " + strategy.name());
            }
        }
    }
}
