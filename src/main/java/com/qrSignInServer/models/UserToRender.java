package com.qrSignInServer.models;

import java.time.LocalDateTime;

public class UserToRender {
    private String username;
    private String email;
    private LocalDateTime createdAt;

    public UserToRender() {
    }

    public UserToRender(String username, String email, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
