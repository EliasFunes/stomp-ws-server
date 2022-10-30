package com.qrSignInServer.controllers;

import com.qrSignInServer.config.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/ws")
public class WSController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/sendToUser")
    public void sendToUser(@RequestHeader HttpHeaders headers, @RequestBody @Valid HashMap<String, String> data) {
        //TODO: ver si se puede refactorizar algo en un utils.
        String tokenQR = data.get("tokenQr");
        String qrId = jwtTokenUtil.getQRIDFromToken(tokenQR);
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        List<Transport> transports =
                Collections.<Transport>singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandlerAdapter handler =
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//                        logger.info("Connected to {}", session);
                        session.send("/app/hello_user", qrId);
                        session.disconnect();
                    }
                    @Override
                    public void handleException(
                            StompSession session,
                            StompCommand command,
                            StompHeaders headers,
                            byte[] payload,
                            Throwable exception) {
                        throw new RuntimeException(exception);
                    }
                };
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("X-Authorization", token);
        connectHeaders.add("username", "username");
        stompClient.connect("ws://127.0.0.1:8080/wsc", handshakeHeaders, connectHeaders, handler, new Object[0]);
//        logger.info("hello done");

    }
}
