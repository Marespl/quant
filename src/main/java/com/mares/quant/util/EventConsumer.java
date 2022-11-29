package com.mares.quant.util;

import com.mares.quant.event.service.EventServiceImpl;

import java.util.concurrent.*;

public class EventConsumer {

    private final BlockingQueue<String> eventsQueue;
    private final BlockingQueue<String> userEventsQueue;

    private final EventServiceImpl eventService;
    private final ExecutorService executorService;

    public EventConsumer(EventServiceImpl eventService) {
        this.eventService = eventService;
        this.eventsQueue = new LinkedBlockingDeque<>();
        this.userEventsQueue = new LinkedBlockingDeque<>();

        this.executorService = Executors.newFixedThreadPool(2);

        init();
    }

    private void init() {
        executorService.execute(() -> {

            while (true) {
                String event = null;
                try {
                    event = eventsQueue.take();
                    eventService.processEvent(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    if (event == null) {
                        return;
                    }
                    eventsQueue.add(event);
                }
            }

        });

        executorService.execute(() -> {
            try {
                while (true) {
                    String event = userEventsQueue.take();
                    eventService.processUserEvent(event);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void receiveUserEvent(String userEvent) {
        userEventsQueue.add(userEvent);
    }

    public void receiveEvent(String event) {
        eventsQueue.add(event);
    }
}
