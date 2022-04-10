package com.qrSignInServer.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import java.io.Serializable;


public class CreateUserRequest implements Serializable {

    private @NotNull @NotBlank @Email String email;
    private @NotNull @NotBlank String username;
    private @NotNull @NotBlank String password;
    private @NotNull @NotBlank String rePassword;


    //need default constructor for JSON Parsing
    public CreateUserRequest(){

    }

    public CreateUserRequest(String email, String username, String password, String rePassword) {
        this.setEmail(email);
        this.setUsername(username);
        this.setPassword(password);
        this.setRePassword(rePassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
}
