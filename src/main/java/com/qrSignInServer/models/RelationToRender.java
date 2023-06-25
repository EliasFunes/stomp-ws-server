package com.qrSignInServer.models;

public class RelationToRender {
    private User lessor;

    private User tenant;

    public RelationToRender(User lessor, User tenant) {
        this.lessor = lessor;
        this.tenant = tenant;
    }

    public User getLessor() {
        return lessor;
    }

    public void setLessor(User lessor) {
        this.lessor = lessor;
    }

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }
}
