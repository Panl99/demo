package com.lp.demo.openfeign.consumer.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        // 设置日志级别为详细
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer feignRetryer() {
        // 自定义重试策略
        return new Retryer.Default(100, 1000, 3);
    }

    @Bean
    public RequestInterceptor authInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer token");
        };
    }
}