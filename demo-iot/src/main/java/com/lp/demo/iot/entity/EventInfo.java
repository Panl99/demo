package com.lp.demo.iot.entity;

import com.alibaba.fastjson.JSONObject;
import com.lp.demo.iot.enums.BusinessTypeEnum;
import com.lp.demo.iot.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lp
 * @date 2021/5/31 11:47
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInfo implements Serializable {

    private static final long serialVersionUID = 9072317311052804180L;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private BusinessTypeEnum businessType;

    /**
     * 事件类型
     */
    @NotNull(message = "事件类型不能为空")
    private EventTypeEnum eventType;

    /**
     * 事件消息内容
     */
    private JSONObject eventContent;

}
