package com.qrSignInServer.services;

import com.qrSignInServer.models.JWT;
import com.qrSignInServer.repositories.JWTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JWTService {

    @Autowired
    JWTRepository jwtRepository;

    public Boolean jwtIsPresent(String jwtparam) {
        return jwtRepository.findByToken(jwtparam).isPresent();
    }

    public Optional<JWT> create(String jwtparam) throws ValidationException {
        if(jwtIsPresent(jwtparam)){
            throw new ValidationException("JWT already exists!");
        }

        JWT jwt = new JWT();
        jwt.setToken(jwtparam);
        jwt.setCreatedAt(LocalDateTime.now());

        Long id = jwtRepository.save(jwt).getId();
        return jwtRepository.findById(id);
    }
}
