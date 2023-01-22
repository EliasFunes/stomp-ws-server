package com.qrSignInServer.controllers;

import com.qrSignInServer.models.WSPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AppController {
    Logger logger = LoggerFactory.getLogger(AppController.class);

    //ESTE SE UTILIZA PARA ENVIAR A TODOS LOS QUE ESTAN SUBSCRIPTOS
    /*@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello" + HtmlUtils.htmlEscape(message.getName()) + "!");
    }*/

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello_user")
     public void send(SimpMessageHeaderAccessor sha, @Payload WSPayload payload) {
        String message = "Hello from " + sha.getUser().getName();
        logger.info(message);
        simpMessagingTemplate.convertAndSendToUser(payload.getQrId(), "/topic/messages", payload.getUserReference());
    }

}
