package com.qrSignInServer.models;

import java.time.LocalDateTime;

public class LogToRender {
    private String qrId;
    private UserToRender lessor;

    private UserToRender tenant;
    private LocalDateTime createdAt;

    public LogToRender(String qrId, UserToRender lessor, UserToRender tenant, LocalDateTime createdAt) {
        this.qrId = qrId;
        this.lessor = lessor;
        this.tenant = tenant;
        this.createdAt = createdAt;
    }

    public String getQrId() {
        return qrId;
    }

    public void setQrId(String qrId) {
        this.qrId = qrId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
