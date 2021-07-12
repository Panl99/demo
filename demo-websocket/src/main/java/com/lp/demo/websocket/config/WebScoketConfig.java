package com.lp.demo.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lp
 * @create 2021/4/25 10:41
 * @description 开启对websocket的支持
 **/
@Configuration
public class WebScoketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
