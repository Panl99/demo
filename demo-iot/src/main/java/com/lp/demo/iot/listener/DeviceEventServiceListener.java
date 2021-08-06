package com.lp.demo.iot.listener;

import com.lp.demo.iot.annotation.BusinessType;
import com.lp.demo.iot.annotation.EventType;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author lp
 * @date 2021/5/31 14:19
 **/
@Component
public class DeviceEventServiceListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String, Object> beans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(BusinessType.class);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>beans = " + beans);
        DeviceEventServiceContext eventServiceContext = contextRefreshedEvent.getApplicationContext().getBean(DeviceEventServiceContext.class);
        beans.forEach((name, bean) -> {
            BusinessType type = bean.getClass().getAnnotation(BusinessType.class);
//            eventServiceContext.putDeviceEventService(type.name().getName(), bean);
            eventServiceContext.putDeviceEventServiceClazz(type.name().getName(), bean.getClass());

            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(EventType.class)) {
                    EventType eventType = method.getAnnotation(EventType.class);
                    eventServiceContext.putDeviceEventServiceMethod(type.name().getName()+"."+eventType.name().getName(), method);
                }
            }

        });
//        System.out.println("DeviceEventServiceContext.HANDLER_MAP = " + DeviceEventServiceContext.HANDLER_MAP);
        System.out.println("DeviceEventServiceContext.CLASS_MAP = " + DeviceEventServiceContext.CLAZZ_MAP);
        System.out.println("DeviceEventServiceContext.METHOD_MAP = " + DeviceEventServiceContext.METHOD_MAP);

    }
}
