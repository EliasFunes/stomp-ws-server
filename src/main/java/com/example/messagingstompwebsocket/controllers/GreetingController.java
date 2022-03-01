package com.example.messagingstompwebsocket.controllers;

import com.example.messagingstompwebsocket.models.Greeting;
import com.example.messagingstompwebsocket.models.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello" + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello_user")
     public void send(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = "Hello from " + sha.getUser().getName();
        System.out.println("to user => " + username);
        System.out.println("from user => " + sha.getUser().getName());
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/messages", message);
    }

}
