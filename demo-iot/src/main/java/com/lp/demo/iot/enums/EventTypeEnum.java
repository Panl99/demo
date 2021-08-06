package com.lp.demo.iot.enums;

/**
 * @author lp
 * @date 2021/5/31 12:05
 **/
public enum EventTypeEnum {
    // 查询设备
    QUERY("query"),
    // 添加设备
    ADD("add"),
    // 设备刷新
    REFRESH("refresh"),
    // 设备同步给app
    SYNC("sync"),
    // 设备删除
    DELETE("delete");
/*    // 设备执行
    EXECUTE("execute"),
    // 设备状态上报
    UPDATE("update"),
    // 解绑
    UNBIND("unbind"),
    // 获取时间
    GET_TIME("get_time"),
    // 网关获取设备
    GET_DEVICE("get_device"),
    // 更新netkey
    UPDATE_NETKEY("update_netkey"),
    // 上报电量
    REPORT("report"),
    // 上报信号强度
    REPORT_SIGNAL("report_signal"),
    // 设备上报网关功能状态
    REPORT_GATEWAY_STATE("report_gateway_state"),
    // 监工上报网关离线
    REPORT_GATEWAY_OFFLINE("report_gateway_offline"),
    // 获取网关和监工信息
    GET_ROLE_ID("get_role_id"),
    // 获取天气
    GET_WEATHER("get_weather"),
    // 网关关闭
    CLOSE_GATEWAY("close_gateway"),
    // 网关在线状态
    ONLINE_STATUS("online_status");*/

    private String name;

    EventTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
