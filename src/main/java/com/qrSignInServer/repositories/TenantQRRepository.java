package com.qrSignInServer.repositories;

import com.qrSignInServer.models.TenantQR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantQRRepository extends JpaRepository<TenantQR, Long> {

    @Override
    Optional<TenantQR> findById(Long id);

    Optional<TenantQR> findByQrID(String qrID);

}
