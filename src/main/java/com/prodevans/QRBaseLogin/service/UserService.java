package com.prodevans.QRBaseLogin.service;

import com.prodevans.QRBaseLogin.configure.CustomHandshakeHandler;
import com.prodevans.QRBaseLogin.configure.CustomStompSessionhandler;
//import com.prodevans.QRBaseLogin.model.User_Detail;
import com.prodevans.QRBaseLogin.model.User_Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class UserService implements UserDetailsService {
    PasswordEncoder passwordEncoder;
    CustomStompSessionhandler customStompSessionhandler;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, CustomStompSessionhandler customStompSessionhandler) {
        this.passwordEncoder = passwordEncoder;
        this.customStompSessionhandler = customStompSessionhandler;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> hashSet=new HashSet<>();
        hashSet.add(new SimpleGrantedAuthority("USER"));
        System.out.println("working and calling security");
        User user = new User("user", passwordEncoder.encode("1234"),hashSet);
        return user;
    }
    public void dataToBeSent(User_Detail message) throws ExecutionException, InterruptedException {
        StompHeaders stompHeaders=new StompHeaders();
        stompHeaders.add("message", String.valueOf(message));
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler=customStompSessionhandler;
        stompClient.connect("ws://192.168.1.41:8080/ws-login",sessionHandler);
        System.out.println("excuritn g11");
        ListenableFuture<StompSession> sessionAsync =stompClient.connect("ws://192.168.1.41:8080/ws-login", sessionHandler);
        StompSession session=sessionAsync.get();
        session.send("/app/send-data",message);

    }
}
