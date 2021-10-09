package com.lp.demo.iot.controller;

import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.dto.RequestHeaderDto;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.common.service.ThreadLocalService;
import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.service.DeviceEventService;
//import org.apache.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author lp
 * @date 2021/8/5 10:43
 **/
@Slf4j
@RestController
public class DeviceManagementRestController {
    //    @Reference
    @Autowired
    DeviceEventService deviceEventService;

//    @Autowired
//    DeviceEventServiceContext eventServiceContext;

    /**
     * 统一执行接口
     *
     * @param eventInfo
     * @param request
     */
    @RequestMapping(value = "/iot/device/execute")
    public JsonResult execute(@RequestBody EventInfo eventInfo,
                              HttpServletRequest request) {
        log.info("device event execute, businessType = {}, eventType = {}", eventInfo.getBusinessType(), eventInfo.getEventType());

        ThreadLocalService.getInstance().setValue(new RequestHeaderDto(request));
        return deviceEventService.execute(eventInfo);
//        eventServiceContext.getDeviceEventService(eventInfo.getBusinessType()).execute(eventInfo);

    }
}
