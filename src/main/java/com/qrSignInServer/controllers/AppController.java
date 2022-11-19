package com.qrSignInServer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AppController {

    //ESTE SE UTILIZA PARA ENVIAR A TODOS LOS QUE ESTAN SUBSCRIPTOS
    /*@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello" + HtmlUtils.htmlEscape(message.getName()) + "!");
    }*/

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    //TODO: en vez de recibir un (@Payload String username) podemos recibir un json con los datos necesarios
    // a parte del qrId y el username
    @MessageMapping("/hello_user")
     public void send(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = "Hello from " + sha.getUser().getName();
        //TODO: en vez de enviar un string como message podemos enviar un json con mas informacion
        //TODO: si pasa la validacion, obtener el identificador que el cliente cargo (tenant)
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/messages", message);

    }

}
