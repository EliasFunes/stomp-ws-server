package com.qrSignInServer.config.webSocket;

import com.qrSignInServer.Interceptors.UserInterceptor;
import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
        /**
         * Prefijo que se utiliza para invocar a los controladores de la aplicacion desde el protocolo websocket.
         */
        config.setApplicationDestinationPrefixes("/app");
        /**
         * Prefijo que se utiliza para enviar los mensajes a los usuarios especificos.
         */
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Endpoint que se utiliza para conectarse al stomp websocket.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/wsc").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor(jwtTokenUtil, jwtUserDetailsService));
    }

}
