package com.lp.demo.iot.listener;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lp
 * @date 2021/5/31 14:13
 * @description 事件类型-处理器映射关系
 **/
@Component
public class DeviceEventServiceContext {
//    public static final Map<String, DeviceEventService> HANDLER_MAP = new HashMap<>();
    // 存放带有EventType注解的方法
    public static final Map<String, Method> EVENT_MAP = new HashMap<>();
    // 存放带有BusinessType注解的类
    public static final Map<String, Class> BUSINESS_MAP = new HashMap<>();

//    public DeviceEventService getDeviceEventService(String type) {
//        return HANDLER_MAP.get(type);
//    }
//
//    public void putDeviceEventService(String type, DeviceEventService deviceEventService) {
//        HANDLER_MAP.put(type, deviceEventService);
//    }

    public Method getDeviceEventServiceMethod(String type) {
        return EVENT_MAP.get(type);
    }
    public void putDeviceEventServiceMethod(String type, Method deviceEventServiceMethod) {
        EVENT_MAP.put(type, deviceEventServiceMethod);
    }

    public Class getDeviceEventServiceClazz(String type) {
        return BUSINESS_MAP.get(type);
    }
    public void putDeviceEventServiceClazz(String type, Class deviceEventServiceClazz) {
        BUSINESS_MAP.put(type, deviceEventServiceClazz);
    }

}
