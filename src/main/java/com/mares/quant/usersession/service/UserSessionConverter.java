package com.mares.quant.usersession.service;

import com.mares.quant.usersession.domain.UserSessionDto;
import com.mares.quant.usersession.repository.UserSessionEntity;

public class UserSessionConverter {

    private UserSessionConverter() {

    }

    public static UserSessionEntity convert(UserSessionDto from) {
        UserSessionEntity result = new UserSessionEntity();
        result.setIp(from.getIp());
        result.setUserId(from.getUserId());
        result.setLocation(from.getLocation());
        result.setSessionEndTimestamp(from.getSessionEndTimestamp());
        result.setSessionStartTimestamp(from.getSessionStartTimestamp());
        result.setVpnEnabled(from.isVpnEnabled());

        return result;
    }

    public static UserSessionDto convert(UserSessionEntity from) {
        UserSessionDto result = new UserSessionDto();
        result.setIp(from.getIp());
        result.setUserId(from.getUserId());
        result.setLocation(from.getLocation());
        result.setSessionEndTimestamp(from.getSessionEndTimestamp());
        result.setSessionStartTimestamp(from.getSessionStartTimestamp());
        result.setVpnEnabled(from.isVpnEnabled());

        return result;
    }

}
