package com.mares.quant.usersession.service;

import com.mares.quant.usersession.domain.UserSessionDto;

public interface UserSessionService {

    UserSessionDto getUserSessionByIpAndTimestamp(String ip, long timestamp);

    void storeSession(UserSessionDto userSessionDto);
}
