package com.qrSignInServer.controllers;

import com.qrSignInServer.dto.RelationRequest;
import com.qrSignInServer.models.Relation;
import com.qrSignInServer.services.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(value = "/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @PostMapping(value = "/")
    public @ResponseBody Optional<Relation> addRelation(@RequestBody @Valid RelationRequest relationRequest) throws ValidationException {
        return relationService.create(relationRequest);
    }

}
