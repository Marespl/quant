package com.mares.quant.generator;

import java.util.*;

public class IPSeeder {

    private final Random random;
    private final List<String> ipPool;
    private final Set<String> inUse;

    public IPSeeder(List<String> ipPool){
        this.ipPool = new ArrayList<>(ipPool);
        this.random = new Random();
        this.inUse = Collections.synchronizedSet(new HashSet<>());
    }

    public synchronized String get(){
        String randomIp = getRandomIp();
        while(inUse.contains(randomIp)){
            randomIp = getRandomIp();
        }
        inUse.add(randomIp);
        return randomIp;
    }

    private String getRandomIp() {
        int randomIndex = random.nextInt(ipPool.size());
        return ipPool.get(randomIndex);
    }

    public void release(String ipAddress){
        inUse.remove(ipAddress);
    }
}
