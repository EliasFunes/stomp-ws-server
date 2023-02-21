package com.qrSignInServer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RelationRequest implements Serializable {

    private @NotNull @NotBlank String lessorToken;
    private @NotNull @NotBlank String reference;

    public RelationRequest() {
    }

    public String getLessorToken() {
        return lessorToken;
    }

    public void setLessorToken(String lessorToken) {
        this.lessorToken = lessorToken;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
