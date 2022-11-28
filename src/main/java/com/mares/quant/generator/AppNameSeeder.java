package com.mares.quant.generator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AppNameSeeder {

    private final Random random;
    private final List<String> appNames;

    public AppNameSeeder(List<String> appNames) {
        this.random = new Random();
        this.appNames = appNames.stream().distinct().collect(Collectors.toList());
    }

    public String get(){
        int randomInt = random.nextInt(appNames.size());
        return appNames.get(randomInt);
    }

}
