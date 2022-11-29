package com.mares.quant.usersession.service;

import com.mares.quant.usersession.domain.UserSessionDto;
import com.mares.quant.usersession.repository.UserSessionEntity;
import com.mares.quant.usersession.repository.UserSessionRepository;

import java.util.Optional;

public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public UserSessionDto getUserSessionByIpAndTimestamp(String ip, long timestamp) {
        return Optional.ofNullable(userSessionRepository.findByIpAndStartLowerEqualThanAndStopGreatEqualThan(ip, timestamp, timestamp))
                .map(UserSessionConverter::convert)
                .orElse(null);
    }

    @Override
    public void storeSession(UserSessionDto userSessionDto) {
        UserSessionEntity entity = UserSessionConverter.convert(userSessionDto);
        userSessionRepository.store(entity);
    }
}
