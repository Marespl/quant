package com.mares.quant.event.service;

public interface EventService {
    void processEvent(String event);

    void processUserEvent(String event);
}
