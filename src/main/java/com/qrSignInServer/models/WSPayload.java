package com.qrSignInServer.models;

public class WSPayload {
    private String qrId;
    private String userReference;

    public WSPayload(){}

    public WSPayload(String qrId, String userReference) {
        this.qrId = qrId;
        this.userReference = userReference;
    }

    public String getQrId() {
        return qrId;
    }

    public void setQrId(String qrId) {
        this.qrId = qrId;
    }

    public String getUserReference() {
        return userReference;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }
}
