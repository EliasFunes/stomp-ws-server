package com.qrSignInServer.Interceptors;

import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.services.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Objects;

@Configurable
public class UserQRInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public UserQRInterceptor(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    Logger logger = LoggerFactory.getLogger(UserQRInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            logger.info("entered into preSend implementation CONNECT");
            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("X-Authorization")).split(" ")[1];
            Object qrIdObj = accessor.getNativeHeader("qrId");
            final String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            String qrId = null;
            if(qrIdObj != null) {
                qrId = accessor.getNativeHeader("qrId").get(0);
            }

            if(!jwtTokenUtil.validateToken(token, userDetails)) {
                throw new BadCredentialsException("Bad credentials for user " + username);
            }


            UsernamePasswordAuthenticationToken user = null;

            if(qrId != null) {
                user = new UsernamePasswordAuthenticationToken(
                                qrId,
                                null,
                                userDetails.getAuthorities()
                        );
            } else {
                 user = new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                null,
                                userDetails.getAuthorities()
                        );
            }


            accessor.setUser(user);
        }
        return message;
    }
}
