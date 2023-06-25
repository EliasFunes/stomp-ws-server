package com.qrSignInServer.models;

public class RelationToRender {
    private UserToRender lessor;

    private UserToRender tenant;

    public RelationToRender(UserToRender lessor, UserToRender tenant) {
        this.lessor = lessor;
        this.tenant = tenant;
    }

    public UserToRender getLessor() {
        return lessor;
    }

    public void setLessor(UserToRender lessor) {
        this.lessor = lessor;
    }

    public UserToRender getTenant() {
        return tenant;
    }

    public void setTenant(UserToRender tenant) {
        this.tenant = tenant;
    }
}
