package com.qrSignInServer.repositories;

import com.qrSignInServer.models.LogReadEventQRCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogReadEventQRCodeRepository extends JpaRepository<LogReadEventQRCode, Long> {
    @Override
    Optional<LogReadEventQRCode> findById(Long id);

    List<LogReadEventQRCode> findByLessor(Long lessor);

}
