package com.qrSignInServer.controllers;

import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.dto.RelationRequest;
import com.qrSignInServer.models.LogToRender;
import com.qrSignInServer.models.Relation;
import com.qrSignInServer.models.RelationToRender;
import com.qrSignInServer.services.RelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(value = "/relation")
public class RelationController {

    Logger logger = LoggerFactory.getLogger(RelationController.class);

    @Autowired
    private RelationService relationService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "")
    public @ResponseBody Optional<Relation> addRelation(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid RelationRequest relationRequest
    ) throws ValidationException {

        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = bearerToken.split(" ")[1];


        logger.info("tenant token => " + token);
        logger.info("lessor token => " + relationRequest.getLessorToken());

        final Long tenantID = jwtTokenUtil.getUserIdFromToken(token);
        final Long lessorID = jwtTokenUtil.getUserIdFromToken(relationRequest.getLessorToken());

        Relation relation = new Relation();
        relation.setTenant(tenantID);
        relation.setLessor(lessorID);
        relation.setReference(relationRequest.getReference());

        return relationService.create(relation);
    }


    @GetMapping(value = "getAllByUser")
    public @ResponseBody List<RelationToRender> allRelationByUser(
            @RequestHeader HttpHeaders headers
    ) throws ValidationException {
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = bearerToken.split(" ")[1];
        final Long userId = jwtTokenUtil.getUserIdFromToken(token);
        return relationService.findByUserId(userId);
    }

    @GetMapping(value = "getAllLogByUserId")
    public @ResponseBody List<LogToRender> allLogByUser(
            @RequestHeader HttpHeaders headers
    ) throws ValidationException {
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = bearerToken.split(" ")[1];
        final Long userId = jwtTokenUtil.getUserIdFromToken(token);
        return relationService.findLogReadQRByUserId(userId);
    }

}
