package com.mares.quant.usersession.repository;

import com.google.gson.Gson;
import com.mares.quant.app.App;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UserSessionRepositoryImpl implements UserSessionRepository {

    private final String STORE_PATH = App.STORE_FOLDER + "/userSession/";

    private final Gson gson = new Gson();

    @Override
    public UserSessionEntity findByIpAndStartLowerEqualThanAndStopGreatEqualThan(String ip, long ts1, long ts2) {
        File file = new File(STORE_PATH + ip + ".txt");
        if (!file.exists()) {
            return null;
        }
        try {
            List<String> entries = FileUtils.readLines(file, "UTF-8");
            return entries.stream()
                    .map(json -> gson.fromJson(json, UserSessionEntity.class))
                    .filter(userSessionEntity -> ts1 >= userSessionEntity.getSessionStartTimestamp() && ts2 <= userSessionEntity.getSessionEndTimestamp())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void store(UserSessionEntity entity) {
        String json = gson.toJson(entity);
        try {
            FileUtils.writeLines(new File(STORE_PATH + entity.getIp() + ".txt"), Collections.singletonList(json), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
