package com.qrSignInServer.models;

import java.time.LocalDateTime;

public class LogToRender {
    private String qrId;
    private User lessor;

    private User tenant;
    private LocalDateTime createdAt;

    public LogToRender(String qrId, User lessor, User tenant, LocalDateTime createdAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
