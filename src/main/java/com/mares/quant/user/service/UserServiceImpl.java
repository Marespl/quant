package com.mares.quant.user.service;

import java.util.Random;


public class UserServiceImpl implements UserService {

    private final Random random = new Random();

    @Override
    public boolean isUserUnderSurveillance(String userId) {
        return random.nextBoolean();
    }
}
