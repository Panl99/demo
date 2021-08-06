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
@BusinessType(name = BusinessTypeEnum.ALI)
public class AliDeviceEventHandler {

    @EventType(name = EventTypeEnum.QUERY)
    public String query(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.query---------eventInfo = "+ eventInfo);

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

    @EventType(name = EventTypeEnum.ADD)
    public void add(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.add---------eventInfo = "+ eventInfo);
    }

    @EventType(name = EventTypeEnum.DELETE)
    public void del(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.del---------eventInfo = "+ eventInfo);
    }
}
