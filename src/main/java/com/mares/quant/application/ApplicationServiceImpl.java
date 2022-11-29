package com.mares.quant.application;

import java.util.Random;


public class ApplicationServiceImpl implements ApplicationService{

    private final Random random = new Random();

    @Override
    public boolean isAppDangerous(String appName) {
        return random.nextBoolean();
    }

    @Override
    public boolean isAppUnknown(String appName) {
        return random.nextBoolean();
    }
}
