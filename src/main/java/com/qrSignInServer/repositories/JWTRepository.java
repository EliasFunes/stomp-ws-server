package com.qrSignInServer.repositories;

import com.qrSignInServer.models.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JWTRepository extends JpaRepository<JWT, Long> {

    @Override
    Optional<JWT> findById(Long aLong);

    Optional<JWT> findByToken(String token);
}
