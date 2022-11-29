package com.mares.quant.event.service;

import com.google.gson.Gson;
import com.mares.quant.application.ApplicationService;
import com.mares.quant.event.domain.EventDto;
import com.mares.quant.event.domain.UserEventDto;
import com.mares.quant.event.domain.UserEventType;
import com.mares.quant.usersession.domain.UserSessionDto;
import com.mares.quant.event.repository.EventEntity;
import com.mares.quant.event.repository.EventRepository;
import com.mares.quant.usersession.service.UserSessionService;
import com.mares.quant.location.service.LocationService;
import com.mares.quant.user.service.UserService;
import com.mares.quant.vpn.VpnService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EventServiceImpl implements EventService {

    private final Gson gson = new Gson();

    private final ApplicationService applicationService;
    private final UserSessionService userSessionService;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final VpnService vpnService;
    private final UserService userService;

    private final Map<String, UserSessionDto> ipUserSessionMap = new ConcurrentHashMap<>();

    public EventServiceImpl(ApplicationService applicationService, UserSessionService userSessionService,
                            LocationService locationService, EventRepository eventRepository,
                            VpnService vpnService, UserService userService) {
        this.applicationService = applicationService;
        this.userSessionService = userSessionService;
        this.locationService = locationService;
        this.eventRepository = eventRepository;
        this.vpnService = vpnService;
        this.userService = userService;
    }

    @Override
    public void processEvent(String eventJson) {

        EventDto eventDto = gson.fromJson(eventJson, EventDto.class);
        String userId = getUserId(eventDto.getIp(), eventDto.getTimestamp());


        boolean appDangerous = applicationService.isAppDangerous(eventDto.getAppName());
        boolean appVerificationRequired = shouldAppBeVerified(userId, eventDto.getAppName());

        EventEntity eventEntity = new EventEntity();
        eventEntity.setIp(eventDto.getIp());
        eventEntity.setUserId(userId);
        eventEntity.setAppDangerous(appDangerous);
        eventEntity.setAppName(eventDto.getAppName());
        eventEntity.setTimestamp(eventDto.getTimestamp());
        eventEntity.setAppShouldBeVerified(appVerificationRequired);

        eventRepository.save(eventEntity);
    }

    private String getUserId(String ip, long timestamp) {
        return Optional.ofNullable(ipUserSessionMap.getOrDefault(ip,
                        userSessionService.getUserSessionByIpAndTimestamp(ip, timestamp)))
                .map(UserSessionDto::getUserId)
                .orElseThrow(() -> new RuntimeException("Session not inited yet."));
    }

    private boolean shouldAppBeVerified(String userId, String appName) {
        return userService.isUserUnderSurveillance(userId) && applicationService.isAppUnknown(appName);
    }

    @Override
    public void processUserEvent(String eventJson) {
        UserEventDto event = gson.fromJson(eventJson, UserEventDto.class);

        if (UserEventType.START.equals(event.getEventType())) {
            UserSessionDto userSessionDto = createUserSessionFromStartEvent(event);
            ipUserSessionMap.put(event.getIp(), userSessionDto);
            return;
        }

        UserSessionDto userSessionDto = ipUserSessionMap.get(event.getIp());
        userSessionDto.setSessionEndTimestamp(event.getTimestamp());
        String location = locationService.getLocation(event.getIp());
        userSessionDto.setLocation(location);
        boolean vpnEnabled = vpnService.isVpnEnabled(event.getIp());
        userSessionDto.setVpnEnabled(vpnEnabled);
        userSessionService.storeSession(userSessionDto);

        ipUserSessionMap.remove(event.getIp());
    }

    private UserSessionDto createUserSessionFromStartEvent(UserEventDto event) {
        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setUserId(event.getUserId());
        userSessionDto.setIp(event.getIp());
        userSessionDto.setSessionStartTimestamp(event.getTimestamp());
        return userSessionDto;
    }
}
