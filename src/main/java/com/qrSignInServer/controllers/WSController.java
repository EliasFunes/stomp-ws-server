package com.qrSignInServer.controllers;

import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.models.Relation;
import com.qrSignInServer.models.TenantQR;
import com.qrSignInServer.models.WSPayload;
import com.qrSignInServer.repositories.RelationRepository;
import com.qrSignInServer.repositories.TenantQRRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.xml.bind.ValidationException;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Controller
@RequestMapping(value = "/ws")
public class WSController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    TenantQRRepository tenantQRRepository;

    @Autowired
    RelationRepository relationRepository;

    Logger logger = LoggerFactory.getLogger(WSController.class);

    //TODO: refactorizar en un util. Tambien poner un try
    private String generateToken(String userReference) {
        //TODO: el secret key habria que obtener del user tenant previamente configurado, de la DB
        String secretKey = "yourSecretKey";
//        String server2Audience = "server2";

        Map<String, Object> claims = new HashMap<>();
        claims.put("userReference", userReference);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    @PostMapping(value = "/sendToUser")
    public void sendToUser(@RequestHeader HttpHeaders headers, @RequestBody @Valid HashMap<String, String> data) throws ValidationException {
        //TODO: ver si se puede refactorizar algo en un utils.

        logger.info("sendToUser");

        String tokenQR = data.get("tokenQr");
        String qrId = jwtTokenUtil.getQRIDFromToken(tokenQR);
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = bearerToken.split(" ")[1];
        Long lessorId = jwtTokenUtil.getUserIdFromToken(token);


        Optional<TenantQR> tenantQR = tenantQRRepository.findByQrID(qrId);

        if(tenantQR.isEmpty()) {
            throw new ValidationException("Relation tenantQR is not present!");
        }

        Long tenantID = tenantQR.get().getTenant();

        Optional<Relation> relation = relationRepository.findByTenantAndLessor(tenantID, lessorId);
        if(relation.isEmpty()){
            throw new ValidationException("Relation tenant and lessor is not present!");
        }

        String userReference = relation.get().getReference();


        List<Transport> transports =
                Collections.<Transport>singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandlerAdapter handler =
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        WSPayload wsPayload = new WSPayload(qrId, generateToken(userReference));
                        session.send("/app/hello_user", wsPayload);
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
        connectHeaders.add("X-Authorization", bearerToken);
        connectHeaders.add("username", "username");
        stompClient.connect("ws://127.0.0.1:8080/wsc", handshakeHeaders, connectHeaders, handler, new Object[0]);
//        logger.info("hello done");

    }
}
