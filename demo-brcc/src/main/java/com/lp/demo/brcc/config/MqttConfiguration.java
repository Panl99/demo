package com.lp.demo.brcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @date 2021/6/8 18:03
 **/
@Configuration
public class MqttConfiguration {
    @Value("${port}")
    private String port;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${topic}")
    private String topic;
    @Value("${queueTopic}")
    private String queueTopic;
    @Value("${qos}")
    private String qos;

    @Value("${rcc.connection-timeout}")
    private long connectionTimeOut;
    @Value("${rcc.read-timeout}")
    private long readTimeOut;

//    @PostConstruct
//    public void init() {
//        System.out.println("load mqtt properties from rcc, the value is: " + port +","+ username +","+ password +","+ topic +","+ queueTopic +","+ qos +","+connectionTimeOut +","+readTimeOut);
//    }

//    @Subscribe
//    public void onChanged(List newValue){
//        System.out.println("MqttConfiguration changed:"+ newValue);
//    }

}
