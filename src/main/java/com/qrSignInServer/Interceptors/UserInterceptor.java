package com.qrSignInServer.Interceptors;

import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.models.User;
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

import javax.xml.bind.ValidationException;
import java.util.Objects;

@Configurable
public class UserInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public UserInterceptor(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            logger.info("entered into preSend implementation CONNECT");
            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("X-Authorization")).split(" ")[1];
            Object qrIdObj = accessor.getNativeHeader("qrId");

            final Long userId = jwtTokenUtil.getUserIdFromToken(token);
            User user = jwtUserDetailsService.loadByUserId(userId);

            String qrId = null;
            if(qrIdObj != null) {
                String tokenQR = accessor.getNativeHeader("qrId").get(0);
                qrId = jwtTokenUtil.getQRIDFromToken(tokenQR);
            }

            try {
                if(!jwtTokenUtil.validateTokenUser(token, user)) {
                    throw new BadCredentialsException("Bad credentials for user " + user.getUsername());
                }
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            UsernamePasswordAuthenticationToken userPAT = null;

            if(qrId != null) {
                userPAT = new UsernamePasswordAuthenticationToken(
                                qrId,
                                null,
                                user.getAuthorities()
                        );
            } else {
                userPAT = new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                user.getAuthorities()
                        );
            }
            accessor.setUser(userPAT);
        }
        return message;
    }
}
