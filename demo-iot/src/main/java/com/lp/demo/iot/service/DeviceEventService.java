package com.lp.demo.iot.service;


import com.lp.demo.iot.entity.EventInfo;

/**
 * @author lp
 * @date 2021/5/31 11:43
 **/
public interface DeviceEventService {
    /**
     * 设备控制
     * @param eventInfo
     */
    String execute(EventInfo eventInfo);
//
//    /**
//     * 添加设备
//     * @param eventInfo
//     */
//    void add(EventInfo eventInfo);
//
//    /**
//     * 删除设备
//     * @param eventInfo
//     */
//    void delete(EventInfo eventInfo);
//
//    /**
//     * 刷新设备
//     * @param eventInfo
//     */
//    void refresh(EventInfo eventInfo);
//
//    /**
//     * 同步设备
//     * @param eventInfo
//     */
//    void sync(EventInfo eventInfo);
//
//    /**
//     * 查询设备
//     * @param eventInfo
//     */
//    void query(EventInfo eventInfo);
//
//    /**
//     * 设备在线
//     * @param eventInfo
//     */
//    void online(EventInfo eventInfo);
//
//    /**
//     * 设备离线
//     * @param eventInfo
//     */
//    void offline(EventInfo eventInfo);

}
