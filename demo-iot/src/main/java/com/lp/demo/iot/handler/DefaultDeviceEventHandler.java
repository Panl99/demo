package com.lp.demo.iot.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.service.CommonService;
import com.lp.demo.iot.annotation.BusinessType;
import com.lp.demo.iot.annotation.EventType;
import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.enums.BusinessTypeEnum;
import com.lp.demo.iot.enums.EventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lp
 * @date 2021/8/13 9:52
 **/
@Component
@BusinessType(value = BusinessTypeEnum.DEFAULT)
public class DefaultDeviceEventHandler {

    /**
     * 调用方法通过反射：deviceEventServiceMethod.invoke(deviceEventServiceClazz.newInstance(), eventInfo); new了对象的实例
     * 因此spring在此处不会注入成功
     * 解决方法：通过@PostConstruct初始化一个静态对象和它的静态成员变量，原理是拿到service层bean对象，静态存储下来，防止被释放。
     */
    @Autowired
    CommonService commonService;

    private static DefaultDeviceEventHandler handler;

    @PostConstruct
    public void init() {
        handler = this;
        handler.commonService = this.commonService;
    }


    @EventType(value = EventTypeEnum.QUERY)
    public String query(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>DefaultDeviceEventHandler.query---------eventInfo = "+ eventInfo);

        JSONObject jsonContent = eventInfo.getEventContent();
        JSONObject header = jsonContent.getJSONObject("header");
        String name = header.getString("name");

        JSONObject payload = jsonContent.getJSONObject("payload");
        String deviceId = payload.getString("deviceId");
        String value = payload.getString("value");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("name is ".concat(name));
        jsonArray.add("value is ".concat(value));
        jsonArray.add("deviceId is ".concat(deviceId));

        return jsonArray.toString();
    }

    @EventType(value = EventTypeEnum.ADD)
    public void add(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>DefaultDeviceEventHandler.add---------eventInfo = "+ eventInfo);
    }

    @EventType(value = EventTypeEnum.DELETE)
    public void del(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>DefaultDeviceEventHandler.del---------eventInfo = "+ eventInfo);
    }
}
