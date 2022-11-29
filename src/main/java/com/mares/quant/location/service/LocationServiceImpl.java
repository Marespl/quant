package com.mares.quant.location.service;

import static java.lang.Thread.sleep;

public class LocationServiceImpl implements LocationService {

    @Override
    public String getLocation(String ip) {
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Poland/Lubuskie";
    }
}
