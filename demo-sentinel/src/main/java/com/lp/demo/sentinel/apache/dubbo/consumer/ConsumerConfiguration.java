package com.lp.demo.sentinel.apache.dubbo.consumer;


import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@DubboComponentScan
public class ConsumerConfiguration {
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("demo-consumer");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return registryConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        // 设置dubbo消费者不启用sentinel，Uncomment below line if you don't want to enable Sentinel for Dubbo service consumers.
        // consumerConfig.setFilter("-sentinel.dubbo.consumer.filter");
        return consumerConfig;
    }

    @Bean
    public FooServiceConsumer annotationDemoServiceConsumer() {
        return new FooServiceConsumer();
    }
}
