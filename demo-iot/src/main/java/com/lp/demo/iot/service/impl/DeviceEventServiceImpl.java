package com.lp.demo.iot.service.impl;

import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.listener.DeviceEventServiceContext;
import com.lp.demo.iot.service.DeviceEventService;
import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lp
 * @date 2021/8/5 9:52
 **/
@Service
@Slf4j
public class DeviceEventServiceImpl implements DeviceEventService {

    @Autowired
    DeviceEventServiceContext eventServiceContext;

    @Override
    public String execute(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>DeviceEventServiceImpl.execute----------");
        String result = null;
        Method deviceEventServiceMethod = eventServiceContext.getDeviceEventServiceMethod(eventInfo.getBusinessType().concat(".").concat(eventInfo.getEventType()));
        Class deviceEventServiceClazz = eventServiceContext.getDeviceEventServiceClazz(eventInfo.getBusinessType());
//        DeviceEventService deviceEventService = eventServiceContext.getDeviceEventService(eventInfo.getBusinessType());
        try {
            result = (String) deviceEventServiceMethod.invoke(deviceEventServiceClazz.newInstance(), eventInfo);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("method invoke error, e = {}", e.getMessage());
        }

        return result;
    }
}
