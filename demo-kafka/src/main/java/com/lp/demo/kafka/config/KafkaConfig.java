package com.lp.demo.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @author lp
 * @create 2021/5/3 23:44
 * @description 程序启动时创建topic
 **/
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin admin(KafkaProperties properties) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        // 默认这个值是False的，在Broker不可用时，不影响Spring 上下文的初始化。如果你觉得Broker不可用影响正常业务需要显示的将这个值设置为True
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("topic-k1", 1, (short) 1);
    }
}
