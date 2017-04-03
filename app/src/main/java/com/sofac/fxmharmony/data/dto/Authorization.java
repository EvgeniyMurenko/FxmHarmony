package com.sofac.fxmharmony.data.dto;


public class Authorization {

    public Authorization(){}

    public Authorization(String ssoId, String password, String googleCloudKey) {
        this.ssoId = ssoId;
        this.password = password;
        GoogleCloudKey = googleCloudKey;
    }

    private String ssoId;

    private String password;

    private String GoogleCloudKey;

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleCloudKey() {
        return GoogleCloudKey;
    }

    public void setGoogleCloudKey(String googleCloudKey) {
        GoogleCloudKey = googleCloudKey;
    }
}
