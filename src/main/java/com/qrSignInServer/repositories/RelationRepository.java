package com.qrSignInServer.repositories;

import com.qrSignInServer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> {

    @Override
    Optional<Relation> findById(Long id);

    Optional<Relation> findByTenantAndLessor(Long tenant, Long lessor);

    List<Relation> findByLessor(Long lessor);

}
