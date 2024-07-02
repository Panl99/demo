package com.lp.demo.spring.event.another;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("anotherEventPublisher")
public class EventPublisher {

    private static ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        EventPublisher.applicationEventPublisher = applicationEventPublisher;
    }

    public static <T extends Event> void publish(T event) {
        applicationEventPublisher.publishEvent(event);
    }
}
