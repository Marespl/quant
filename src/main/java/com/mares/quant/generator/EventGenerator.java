package com.mares.quant.generator;

import com.google.gson.Gson;
import com.mares.quant.event.domain.EventDto;
import com.mares.quant.util.EventConsumer;
import com.mares.quant.event.domain.UserEventDto;
import com.mares.quant.event.domain.UserEventType;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class EventGenerator {

    private static final int EVENTS_PER_MINUTE_THRESHOLD = 10000;
    private static final int DURATION_FRAME_IN_SECONDS = 1;

    private final Random random = new Random();
    private final Gson gson = new Gson();
    private final IPSeeder ipSeeder;
    private final AppNameSeeder appNameSeeder;

    private boolean running = false;

    private int eventsCounter = 0;
    private Instant timeCountingStart;

    public EventGenerator(IPSeeder ipSeeder, AppNameSeeder appNameSeeder) {
        this.ipSeeder = ipSeeder;
        this.appNameSeeder = appNameSeeder;
    }

    public void stop() {
        running = false;
    }

    public synchronized void start(EventConsumer eventConsumer) {
        resetCounters();

        running = true;
        while (running) {
            if (passedLimitPerMinute()) {
                if (shouldStartNewTimeFrame()) {
                    resetCounters();
                }
                continue;
            }
            String userId = UUID.randomUUID().toString();
            String ip = ipSeeder.get();
            UserEventDto userStartEvent = generateUserEvent(userId, ip, UserEventType.START);
            eventConsumer.receiveUserEvent(gson.toJson(userStartEvent));

            int randomInt = random.nextInt(1000);

            for (int i = 0; i < randomInt; i++) {
                EventDto eventDto = generateEvent(ip);
                eventConsumer.receiveEvent(gson.toJson(eventDto));
                eventsCounter++;
            }
            UserEventDto userStopEvent = generateUserEvent(userId, ip, UserEventType.STOP);
            eventConsumer.receiveUserEvent(gson.toJson(userStopEvent));
        }
    }

    private boolean shouldStartNewTimeFrame() {
        Duration duration = Duration.between(timeCountingStart, Instant.now());
        return duration.getSeconds() >= DURATION_FRAME_IN_SECONDS;
    }

    private boolean passedLimitPerMinute() {
        return eventsCounter >= EVENTS_PER_MINUTE_THRESHOLD;
    }

    private void resetCounters() {
        timeCountingStart = Instant.now();
        eventsCounter = 0;
    }

    private EventDto generateEvent(String ip) {
        EventDto eventDto = new EventDto();
        eventDto.setIp(ip);
        eventDto.setTimestamp(System.currentTimeMillis());
        eventDto.setAppName(appNameSeeder.get());

        return eventDto;
    }

    private UserEventDto generateUserEvent(String userId, String ip, UserEventType userEventType) {
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setEventType(userEventType);
        userEventDto.setTimestamp(System.currentTimeMillis());
        userEventDto.setUserId(userId);
        userEventDto.setIp(ip);
        return userEventDto;
    }
}
