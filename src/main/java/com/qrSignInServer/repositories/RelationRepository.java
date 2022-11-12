package com.qrSignInServer.repositories;

import com.qrSignInServer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation, Long> {
}
