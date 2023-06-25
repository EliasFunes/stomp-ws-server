package com.qrSignInServer.services;

import com.qrSignInServer.models.*;
import com.qrSignInServer.repositories.LogReadEventQRCodeRepository;
import com.qrSignInServer.repositories.RelationRepository;

import com.qrSignInServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelationService {
    @Autowired
    RelationRepository relationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LogReadEventQRCodeRepository logReadEventQRCodeRepository;

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

    public List<RelationToRender> findByUserId(Long id) throws ValidationException {

        List<Relation> listRelation = relationRepository.findByLessor(id);
        List<RelationToRender> listRelationToRender =
                listRelation.stream().map(relation -> {
                    User lessor = userRepository.findByIdAndTipo(relation.getLessor(), "lessor").orElse(null);
                    User tenant = userRepository.findByIdAndTipo(relation.getTenant(), "tenant").orElse(null);

                    UserToRender lessorToRender =
                            new UserToRender(lessor.getUsername(), lessor.getEmail(), lessor.getCreatedAt());
                    UserToRender tenantToRender =
                            new UserToRender(tenant.getUsername(), tenant.getEmail(), tenant.getCreatedAt());

                    RelationToRender rtr = new RelationToRender(lessorToRender, tenantToRender);
                    return rtr;
                }).collect(Collectors.toList());

        return listRelationToRender;
    }

    public List<LogToRender> findLogReadQRByUserId(Long id) throws ValidationException {
        List<LogReadEventQRCode> listLogs = logReadEventQRCodeRepository.findByLessor(id);
        List<LogToRender> listLogsToRender =
                listLogs.stream().map(log -> {
                    User lessor = userRepository.findByIdAndTipo(log.getLessor(), "lessor").orElse(null);
                    User tenant = userRepository.findByIdAndTipo(log.getTenant(), "tenant").orElse(null);

                    UserToRender lessorToRender =
                            new UserToRender(lessor.getUsername(), lessor.getEmail(), lessor.getCreatedAt());
                    UserToRender tenantToRender =
                            new UserToRender(tenant.getUsername(), tenant.getEmail(), tenant.getCreatedAt());

                    LogToRender ltr = new LogToRender(log.getQrId(), lessorToRender, tenantToRender, log.getCreatedAt());
                    return ltr;
                }).collect(Collectors.toList());
        return listLogsToRender;
    }
}
