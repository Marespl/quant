package com.mares.quant.usersession.repository;

public interface UserSessionRepository {

    UserSessionEntity findByIpAndStartLowerEqualThanAndStopGreatEqualThan(String ip, long ts1, long ts2);

    void store(UserSessionEntity entity);
}
