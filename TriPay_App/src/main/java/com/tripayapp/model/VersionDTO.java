package com.tripayapp.model;


public class VersionDTO {
    private String versionCode;
    private String subVersionCode;
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getSubVersionCode() {
        return subVersionCode;
    }

    public void setSubVersionCode(String subVersionCode) {
        this.subVersionCode = subVersionCode;
    }
}
