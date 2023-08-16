package com.prodevans.QRBaseLogin.controller;

import com.prodevans.QRBaseLogin.configure.CustomStompSessionhandler;
import com.prodevans.QRBaseLogin.model.User_Detail;
import com.prodevans.QRBaseLogin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

@Controller
public class RegisterController {

    private CustomStompSessionhandler customStompSessionhandler;
    private SimpMessagingTemplate simpMessagingTemplate;
    private UserService userService;
    @Autowired
    public RegisterController(CustomStompSessionhandler customStompSessionhandler, SimpMessagingTemplate simpMessagingTemplate,UserService userService) {
        this.customStompSessionhandler = customStompSessionhandler;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService=userService;
    }
    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) throws ExecutionException, InterruptedException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        System.out.println(username);
        User_Detail userDetail =new User_Detail();
        userDetail.setSessionid(username);
//        userService.dataToBeSent(userDetail);
    }


    @MessageMapping("/send")
    @SendTo("/topic/successfull")
    public User_Detail send(User_Detail Message){
        System.out.println(Message);
        return Message;
    }
    @MessageMapping("/send-data")
    @SendTo("/topic/send-data")
    public void sendData(User_Detail Message){
        System.out.println(Message);
        simpMessagingTemplate.convertAndSendToUser(Message.getSessionid(),"/topic/send-data",Message);
//        return Message;
    }
    @GetMapping("/receiver")
    public ResponseEntity<Integer> getMessage(@RequestParam("text") String message, @RequestParam("username")String username, @RequestParam("password")String password, HttpServletRequest request) throws ExecutionException, InterruptedException {
        User_Detail usr=new User_Detail(message,username,password);
        StompHeaders stompHeaders=new StompHeaders();
        System.out.println(request.getRemoteAddr()+""+request.getRemoteHost()+""+request.getRemoteHost());
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler=customStompSessionhandler;
        ListenableFuture<StompSession> sessionAsync =stompClient.connect("ws://192.168.1.34:8080/ws-login", sessionHandler);
       StompSession session=sessionAsync.get();
       session.send("/app/send-data",usr);
        return new ResponseEntity<Integer>(HttpStatus.OK);
    }
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        HttpSession session= request.getSession();
        System.out.println(session.getId());
        System.out.println("calling");
        return "index";
    }
    @GetMapping("/success")
    public String login(){
        System.out.println("excuted");
        return "success";
    }

}
