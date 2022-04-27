package com.lp.demo.iot.mqtt.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lp
 * @date 2022/4/27 10:33
 * @link https://github.com/emqx/emqx-web-hook
 * @link https://www.emqx.io/docs/zh/v4.3/advanced/webhook.html
 **/
@RestController
@Slf4j
public class EMQWebHookController {

    /**
     * body参数
     */
    // 事件名称
    private static final String ACTION = "action";
    // 客户端id
    private static final String CLIENT_ID = "clientid";
    // 客户端username
    private static final String USER_NAME = "username";

    // 错误原因
    private static final String REASON = "reason";

    /**
     * 事件类型
     */
    // 断连事件
    private static final String ACTION_CLIENT_CONNECTED = "on_client_connected";
    private static final String ACTION_CLIENT_DISCONNECTED = "on_client_disconnected";

    /**
     * 客户端断开链接错误码
     */
    // MQTT客户端正常断开
    private static final String REASON_NORMAL = "normal";
    // MQTT keepalive 超时
    private static final String REASON_KEEPALIVE_TIMEOUT = "keepalive_timeout";
    // TCP客户端断开连接（客户端发来的FIN，但没收到 MQTT DISCONNECT）
    private static final String REASON_CLOSED = "closed"; // tcp_closed
    // EMQX 想向客户端发送一条消息，但是Socket 已经断开
    private static final String REASON_EINVAL = "einval";
    // MQTT 报文格式错误
    private static final String REASON_FUNCTION_CLAUSE = "function_clause";
    // TCP 发送超时（没有收到TCP ACK 回应）
    private static final String REASON_ETIMEDOUT = "etimedout";
    // 在已经有一条MQTT连接的情况下重复收到了MQTT连接请求
    private static final String REASON_PROTO_UNEXPECTED_C = "proto_unexpected_c";
    // TCP 连接建立 15s 之后，还没收到 connect 报文
    private static final String REASON_IDLE_TIMEOUT = "idle_timeout";

    private final Map<String, Boolean> clientStatusMap = new ConcurrentHashMap<>();

    /**
     * EMQ webhook事件处理
     * @param message
     */
    @PostMapping("/emq/webhook")
    public void webHookHandler(@RequestBody JSONObject message) {
        log.info("emq webhook message arrived! message = {}", message);
        String action = message.getString(ACTION);
        String clientId = message.getString(CLIENT_ID);

        switch (action) {
            case ACTION_CLIENT_DISCONNECTED:
                String reason = message.getString(REASON);
                log.info("Event: {}", REASON_KEEPALIVE_TIMEOUT);
                if (reason.equals(REASON_KEEPALIVE_TIMEOUT)) {
                    // TODO
                }

                clientStatusMap.put(clientId, Boolean.FALSE);
                break;
            case ACTION_CLIENT_CONNECTED:
                clientStatusMap.put(clientId, Boolean.TRUE);
                break;
            default:
                log.error("Unsupported action!");
                break;
        }
    }

    /**
     * 获取客户端状态
     *
     * @param clientId 客户端id
     * @return
     */
    @GetMapping("/emq/client/{clientId}/status")
    public boolean getClientStatus(@PathVariable String clientId) {
        log.info("Get client status, clientId = {}", clientId);
        if (clientStatusMap.containsKey(clientId)) {
            return clientStatusMap.get(clientId);
        }
        return false;
    }
}
