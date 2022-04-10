package com.qrSignInServer.models;

import java.security.Principal;

public class UserQR implements Principal {

    private String name;

    public UserQR(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
