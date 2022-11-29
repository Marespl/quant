package com.mares.quant.usersession.domain;

public class UserSessionDto {

    private String userId;
    private String ip;
    private long sessionStartTimestamp;
    private long sessionEndTimestamp;
    private String location;
    private boolean vpnEnabled;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getSessionStartTimestamp() {
        return sessionStartTimestamp;
    }

    public void setSessionStartTimestamp(long sessionStartTimestamp) {
        this.sessionStartTimestamp = sessionStartTimestamp;
    }

    public long getSessionEndTimestamp() {
        return sessionEndTimestamp;
    }

    public void setSessionEndTimestamp(long sessionEndTimestamp) {
        this.sessionEndTimestamp = sessionEndTimestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVpnEnabled() {
        return vpnEnabled;
    }

    public void setVpnEnabled(boolean vpnEnabled) {
        this.vpnEnabled = vpnEnabled;
    }
}
