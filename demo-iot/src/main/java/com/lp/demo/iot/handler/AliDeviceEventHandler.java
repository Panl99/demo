package com.lp.demo.iot.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.dto.RequestHeaderDto;
import com.lp.demo.common.service.ThreadLocalService;
import com.lp.demo.iot.annotation.BusinessType;
import com.lp.demo.iot.annotation.EventType;
import com.lp.demo.iot.entity.EventInfo;
import com.lp.demo.iot.enums.BusinessTypeEnum;
import com.lp.demo.iot.enums.EventTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author lp
 * @date 2021/8/5 9:52
 **/
@Slf4j
@Component
@BusinessType(value = BusinessTypeEnum.ALI)
public class AliDeviceEventHandler {

    @EventType(value = EventTypeEnum.QUERY)
    public String query(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.query---------eventInfo = "+ eventInfo);

        RequestHeaderDto header = (RequestHeaderDto) ThreadLocalService.getInstance().getAndRemove();

        log.info("++++++++++++++ali query and destroy thread local value: = null(true) ++++++++++++++++thread local get value = {}", ThreadLocalService.getInstance().getValue());

        JSONObject jsonContent = eventInfo.getEventContent();
        JSONObject payload = jsonContent.getJSONObject("payload");
        String deviceId = payload.getString("deviceId");
        String name = payload.getString("name");
        String value = payload.getString("value");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("event is ".concat(eventInfo.getBusinessType() + "." + eventInfo.getEventType()));
        jsonArray.add("name is ".concat(name));
        jsonArray.add("value is ".concat(value));
        jsonArray.add("deviceId is ".concat(deviceId));
        jsonArray.add("header is ".concat(header.toString()));

        return jsonArray.toJSONString();
    }

    @EventType(value = EventTypeEnum.ADD)
    public void add(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.add---------eventInfo = "+ eventInfo);
    }

    @EventType(value = EventTypeEnum.DELETE)
    public void del(EventInfo eventInfo) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>AliDeviceEventHandler.del---------eventInfo = "+ eventInfo);
    }
}
