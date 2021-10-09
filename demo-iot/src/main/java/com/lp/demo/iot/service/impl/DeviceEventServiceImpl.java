package com.lp.demo.iot.service.impl;

import com.lp.demo.common.result.JsonResult;
import com.lp.demo.common.result.ResultEnum;
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
    public JsonResult execute(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>DeviceEventServiceImpl.execute----------");
        String result;
        Method deviceEventServiceMethod = eventServiceContext.getDeviceEventServiceMethod(eventInfo.getBusinessType()+(".")+(eventInfo.getEventType()));
        Class deviceEventServiceClazz = eventServiceContext.getDeviceEventServiceClazz(eventInfo.getBusinessType().name());
//        DeviceEventService deviceEventService = eventServiceContext.getDeviceEventService(eventInfo.getBusinessType());

        if (deviceEventServiceClazz == null) {
            log.error("cannot find business handler, businessClazz = {}", eventInfo.getBusinessType());
            return new JsonResult<>(ResultEnum.FAIL, "cannot find business handler, businessClazz = "+ eventInfo.getBusinessType());
        }

        if (deviceEventServiceMethod == null) {
            log.error("cannot find event handling method, eventMethod = {}", eventInfo.getBusinessType() + "." + eventInfo.getEventType());
            return new JsonResult<>(ResultEnum.FAIL, "cannot find event handling method, eventMethod = "+ eventInfo.getBusinessType() + "." + eventInfo.getEventType());
        }

        try {
            result = (String) deviceEventServiceMethod.invoke(deviceEventServiceClazz.newInstance(), eventInfo);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("method invoke error, e = {}", e.getMessage());
            return new JsonResult<>(ResultEnum.FAIL);
        }

        return new JsonResult<>(ResultEnum.SUCCESS, result);
    }
}
