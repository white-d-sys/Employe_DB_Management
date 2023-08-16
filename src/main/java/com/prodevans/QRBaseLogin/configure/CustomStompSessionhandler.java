package com.prodevans.QRBaseLogin.configure;

import com.prodevans.QRBaseLogin.model.User_Detail;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

@Configuration
public class CustomStompSessionhandler extends StompSessionHandlerAdapter {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return User_Detail.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received : " + ((User_Detail) payload).getUsername());
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println(" this is session id for websocket"+session.getSessionId());
    }

}
