package com.lp.demo.iot.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String businessType;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件消息内容
     */
    private JSONObject eventContent;

}
