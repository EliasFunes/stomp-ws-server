package com.qrSignInServer.repositories;

import com.qrSignInServer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByTipo(String tipo);
    Optional<User> findByUsernameAndTipo(String username, String tipo);
}
