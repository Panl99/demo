package com.lp.demo.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.ResolvableType;

import java.util.function.Predicate;

/**
 * @author lp
 * @date 2022/10/12 17:54
 * @desc
 **/
public class UserInfoEventMulticaster implements ApplicationEventMulticaster {

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {

    }

    @Override
    public void addApplicationListenerBean(String s) {

    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {

    }

    @Override
    public void removeApplicationListenerBean(String s) {

    }

    @Override
    public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {

    }

    @Override
    public void removeApplicationListenerBeans(Predicate<String> predicate) {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void multicastEvent(ApplicationEvent applicationEvent) {

    }

    @Override
    public void multicastEvent(ApplicationEvent applicationEvent, ResolvableType resolvableType) {

    }
}
