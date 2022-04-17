package com.qrSignInServer.Interceptors;

import com.qrSignInServer.config.security.JwtTokenUtil;
//import com.qrSignInServer.models.UserQR;
import com.qrSignInServer.services.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@Configurable
public class UserQRInterceptor implements ChannelInterceptor {

    Logger logger = LoggerFactory.getLogger(UserQRInterceptor.class);

    /*@Autowired
    private JwtTokenUtil jwtTokenUtil;*/

    /*@Autowired
    private JwtUserDetailsService jwtUserDetailsService;*/ //TODO: No se porque no funcionan las funciones de los services y components inyectados, necesitamos el tokenutil y el jwtUserDetailService


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {


            logger.info("entered into preSend implementation CONNECT");
            logger.info(accessor.toString());

            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("X-Authorization")).split(" ")[1];

            logger.info("tokenHeader: " + token);

            logger.info("secret: " + "EliasFJulioG");

            //TODO: tampoco funciona el @Value("${jwt.secret}")
            //    private String secret; por eso le paso en duro el secret que esta en .env

            final String username = Jwts.parser().setSigningKey("EliasFJulioG").parseClaimsJws(token).getBody().getSubject();


            //TODO: se deberia usar este
            //final String username = jwtTokenUtil.getUsernameFromToken(token);

            logger.info("username: " + username);

            //TODO: y usar este tambien
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);


//            logger.info("username2:" + userDetails.getUsername());


            //TODO: y usar este tambien
            /*if(!jwtTokenUtil.validateToken(token, userDetails)) {
                throw new BadCredentialsException("Bad credentials for user " + username);
            }*/


            if(!true) {
                logger.info("isInvalid");
                throw new BadCredentialsException("Bad credentials for user " + username);
            }


            UsernamePasswordAuthenticationToken user =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singleton((GrantedAuthority) () -> "USER")
                    );

            accessor.setUser(user);

            //TODO: en ralidad se supone que tengamos que usar los mismo que en el JwtTokenFilter.java

           /* Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                Object name = ((Map) raw).get("username");

                if (name instanceof ArrayList) {
//                accessor.setUser(new UserQR(((ArrayList<String>) name).get(0).toString()));
//                UsernamePasswordAuthenticationToken user =
//                        new UsernamePasswordAuthenticationToken(
//                                ((ArrayList<String>) name).get(0).toString(),
//                                null,
//                                Collections.singleton((GrantedAuthority) () -> "USER")
//                        );
//
//                accessor.setUser(user);


                }
            }*/
        }

        logger.info("retornando: ");
        return message;
    }
}
