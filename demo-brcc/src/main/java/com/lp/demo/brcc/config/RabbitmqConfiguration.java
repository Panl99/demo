package com.lp.demo.brcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author lp
 * @date 2021/6/9 22:52
 * @description
 **/
@Configuration
public class RabbitmqConfiguration {

    @Value("${host}")
    private String host;
    @Value("${port}")
    private String port;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${image}")
    private String image;
    @Value("${container_name}")
    private String containerName;
    @Value("${volumes}")
    private String volumes;
    @Value("${ports}")
    private String ports;
    @Value("${queue}")
    private String queue;


//    @PostConstruct
//    public void init() {
//        System.out.println("load rabbitmq properties from rcc, the value is " + ObjectUtil.getAllFieldValueByName(RabbitmqConfiguration.class));
//    }

//    @Bean
//    public ConfigItemChangedCallable configItemChangedCallable() {
//        return new RabbitmqConfigItemChangedCallable();
//    }
}
