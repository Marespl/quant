package com.mares.quant.application;

public interface ApplicationService {

    boolean isAppDangerous(String appName);

    boolean isAppUnknown(String appName);
}
