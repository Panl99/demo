package com.lp.demo.spring.event;

import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2022/10/12 15:34
 * @desc 事件发布服务
 **/
@Component
public class EventPublisher implements /*ApplicationEventPublisherAware,*/ ApplicationContextAware {

//    private ApplicationEventPublisher applicationEventPublisher;
    private ApplicationContext applicationContext;

//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.applicationEventPublisher = applicationEventPublisher;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> void publishEvent(Event<T> event) {
        ConsoleColorUtil.printDefaultColor(">>>>>>>>>EventPublisher.publishEvent()");
//        applicationEventPublisher.publishEvent(event);
        applicationContext.publishEvent(event);
    }
}
