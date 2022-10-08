package com.qrSignInServer.services;


import com.qrSignInServer.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.qrSignInServer.models.User;
import static java.lang.String.format;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.info("loadUserByUsername ");
//        logger.info("username: " + username);
        return /*(UserDetails) no es necesario castear si el model extiende de esta clase*/
                userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", username)
                        )
                );
    }

    public User loadUserByUsernameAndTipo(String username, String tipo) throws UsernameNotFoundException {
//        logger.info("loadUserByUsername ");
//        logger.info("username: " + username);
        return /*(UserDetails) no es necesario castear si el model extiende de esta clase*/
                userRepository
                        .findByUsernameAndTipo(username, tipo)
                        .orElseThrow(
                                () -> new UsernameNotFoundException(
                                        format("User: %s, not found by tipo: %s", username, tipo)
                                )
                        );
    }

    public User loadByUserId(Long id) throws UsernameNotFoundException {
        return
                userRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new UsernameNotFoundException(
                                        format("User: %s, not found", id.toString())
                                )
                        );
    }
}
