package com.mares.quant.event.repository;

public class EventEntity {

    private long timestamp;
    private String ip;
    private String appName;
    private String userId;

    private boolean appShouldBeVerified;
    private boolean appDangerous;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAppShouldBeVerified() {
        return appShouldBeVerified;
    }

    public void setAppShouldBeVerified(boolean appShouldBeVerified) {
        this.appShouldBeVerified = appShouldBeVerified;
    }

    public boolean isAppDangerous() {
        return appDangerous;
    }

    public void setAppDangerous(boolean appDangerous) {
        this.appDangerous = appDangerous;
    }
}
