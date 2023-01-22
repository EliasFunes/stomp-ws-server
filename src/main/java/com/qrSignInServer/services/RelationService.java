package com.qrSignInServer.services;

import com.qrSignInServer.models.Relation;
import com.qrSignInServer.models.User;
import com.qrSignInServer.repositories.RelationRepository;

import com.qrSignInServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@Service
public class RelationService {
    @Autowired
    RelationRepository relationRepository;

    @Autowired
    UserRepository userRepository;

    public Optional<Relation> create(Relation request) throws ValidationException {

        User lessor =  userRepository.getById(request.getLessor());
        User tenant =  userRepository.getById(request.getTenant());

        if(lessor == null){
            throw new ValidationException("Lessor is not present!");
        } else if(lessor.getTipo().equals("tenant")){
            throw new ValidationException("Lessor is not lessor!");
        }

        if(tenant == null){
            throw new ValidationException("Tenant is not present!");
        } else if(tenant.getTipo().equals("lessor")){
            throw new ValidationException("Tenant is not tenant!");
        }

        Relation relation = new Relation();
        relation.setLessor(lessor.getId());
        relation.setTenant(tenant.getId());
        relation.setReference(request.getReference());
        Long id = relationRepository.save(relation).getId();
        return relationRepository.findById(id);
    }
}
