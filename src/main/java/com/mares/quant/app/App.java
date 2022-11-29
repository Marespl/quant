package com.mares.quant.app;

import com.mares.quant.application.ApplicationService;
import com.mares.quant.application.ApplicationServiceImpl;
import com.mares.quant.event.repository.EventRepository;
import com.mares.quant.event.repository.EventRepositoryImpl;
import com.mares.quant.location.service.LocationService;
import com.mares.quant.location.service.LocationServiceImpl;
import com.mares.quant.user.service.UserService;
import com.mares.quant.user.service.UserServiceImpl;
import com.mares.quant.usersession.repository.UserSessionRepositoryImpl;
import com.mares.quant.usersession.service.UserSessionService;
import com.mares.quant.usersession.service.UserSessionServiceImpl;
import com.mares.quant.util.EventConsumer;
import com.mares.quant.event.service.EventServiceImpl;
import com.mares.quant.generator.AppNameSeeder;
import com.mares.quant.generator.EventGenerator;
import com.mares.quant.generator.IPSeeder;
import com.mares.quant.vpn.VpnService;
import com.mares.quant.vpn.VpnServiceImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class App {

    public static final String STORE_FOLDER = "D:\\DB";


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        URI appNamesUri = App.class.getClassLoader().getResource("AppNames.txt").toURI();
        URI ipsUri = App.class.getClassLoader().getResource("IPs.txt").toURI();

        List<String> appNames = FileUtils.readLines(new File(appNamesUri), "UTF-8");
        List<String> ips = FileUtils.readLines(new File(ipsUri), "UTF-8");


        AppNameSeeder appNameSeeder = new AppNameSeeder(appNames);
        IPSeeder ipSeeder = new IPSeeder(ips);


        EventGenerator eventGenerator = new EventGenerator(ipSeeder, appNameSeeder);

        ApplicationService dangerousAppsService = new ApplicationServiceImpl();
        UserSessionService userSessionService = new UserSessionServiceImpl(new UserSessionRepositoryImpl());
        LocationService locationService = new LocationServiceImpl();
        EventRepository eventRepository = new EventRepositoryImpl();
        VpnService vpnService = new VpnServiceImpl();
        UserService userService = new UserServiceImpl();


        EventServiceImpl eventService = new EventServiceImpl(dangerousAppsService, userSessionService, locationService, eventRepository,
                vpnService, userService);
        EventConsumer eventConsumer = new EventConsumer(eventService);


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> eventGenerator.start(eventConsumer));

        sleep(30000);

        eventGenerator.stop();

    }
}
