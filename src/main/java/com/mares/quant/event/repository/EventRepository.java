package com.mares.quant.event.repository;

import java.util.List;

public interface EventRepository {

    void save(EventEntity eventEntity);


    void save(List<EventEntity> eventEntity);
}
