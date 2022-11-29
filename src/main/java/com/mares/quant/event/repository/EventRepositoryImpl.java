package com.mares.quant.event.repository;

import com.google.gson.Gson;
import com.mares.quant.app.App;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class EventRepositoryImpl implements EventRepository {

    private static final String STORE_PATH = App.STORE_FOLDER + "/event/";

    public Instant creationTime = Instant.now();
    public AtomicLong atomicLong = new AtomicLong(0);


    private final Gson gson = new Gson();

    @Override
    public void save(EventEntity eventEntity) {
        String json = gson.toJson(eventEntity);
        try {
            FileUtils.writeLines(new File(STORE_PATH + eventEntity.getUserId() + ".txt"), Collections.singletonList(json), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long currentVal = atomicLong.incrementAndGet();

        if (currentVal % 10000 == 0) {
            Instant now = Instant.now();

            long milis = Duration.between(creationTime, now).toMillis();
            creationTime = Instant.now();
            System.out.println("It took " + milis + " ms to store 10000 events.");
        }

    }

    @Override
    public void save(List<EventEntity> eventEntity) {

    }

}
