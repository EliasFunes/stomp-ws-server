package com.qrSignInServer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RelationRequest implements Serializable {

    private @NotNull @NotBlank Long lessor;
    private @NotNull @NotBlank Long tenant;
    private @NotNull @NotBlank String reference;

    public RelationRequest() {
    }

    public Long getLessor() {
        return lessor;
    }

    public void setLessor(Long lessor) {
        this.lessor = lessor;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
