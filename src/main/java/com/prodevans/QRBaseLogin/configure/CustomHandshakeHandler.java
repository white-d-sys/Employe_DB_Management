package com.prodevans.QRBaseLogin.configure;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    public CustomHandshakeHandler(RequestUpgradeStrategy upgradeStrategy) {
        super(upgradeStrategy);
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("working");
        return new UserPrincipal(UUID.randomUUID().toString());
    }
}
