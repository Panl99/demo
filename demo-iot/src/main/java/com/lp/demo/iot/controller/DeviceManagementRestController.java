package com.lp.demo.iot.controller;

import com.alibaba.fastjson.JSONObject;
import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.service.DeviceEventService;
//import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author lp
 * @date 2021/8/5 10:43
 **/
@RestController
public class DeviceManagementRestController {
//    @Reference
    @Autowired
    DeviceEventService deviceEventService;

//    @Autowired
//    DeviceEventServiceContext eventServiceContext;

    /**
     * 测试接口
     * @param businessType
     * @param eventType
     */
    @RequestMapping(value = "/test/{businessType}/{eventType}", method = RequestMethod.GET)
    public void test(@PathVariable String businessType, @PathVariable String eventType) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEventType(eventType.toLowerCase(Locale.ROOT));
        eventInfo.setBusinessType(businessType.toLowerCase(Locale.ROOT));

        Map eventContentMap = new HashMap();
        eventContentMap.put("header", new HashMap() {{
            put("name", "switch");
        }});
        eventContentMap.put("payload", new HashMap() {{
            put("deviceId", "abc123456");
            put("value", "on");
        }});
        JSONObject jsonObject = new JSONObject(eventContentMap);
        eventInfo.setEventContent(jsonObject);

        String result = deviceEventService.execute(eventInfo);
        System.out.println("result = " + result);
//        eventServiceContext.getDeviceEventService(eventInfo.getBusinessType()).execute(eventInfo);

    }
}
