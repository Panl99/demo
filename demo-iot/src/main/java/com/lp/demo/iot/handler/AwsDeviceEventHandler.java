package com.lp.demo.iot.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.iot.annotation.BusinessType;
import com.lp.demo.iot.annotation.EventType;
import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.enums.BusinessTypeEnum;
import com.lp.demo.iot.enums.EventTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2021/8/5 9:52
 **/
@Component
@BusinessType(value = BusinessTypeEnum.AWS)
public class AwsDeviceEventHandler {

    @EventType(value = EventTypeEnum.QUERY)
    public String query(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AwsDeviceEventHandler.query---------eventInfo = "+ eventInfo);

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
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AwsDeviceEventHandler.add---------eventInfo = "+ eventInfo);
    }

    @EventType(value = EventTypeEnum.DELETE)
    public void del(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AwsDeviceEventHandler.del---------eventInfo = "+ eventInfo);
    }


}
