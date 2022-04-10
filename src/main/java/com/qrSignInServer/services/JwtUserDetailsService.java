package com.qrSignInServer.services;

import com.qrSignInServer.dto.CreateUserRequest;
import com.qrSignInServer.models.User;
import com.qrSignInServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.ValidationException;

import java.util.Optional;

import static java.lang.String.format;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return /*(UserDetails) no es necesario castear si el model extiende de esta clase*/
                userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", username)
                        )
                );
    }
}
