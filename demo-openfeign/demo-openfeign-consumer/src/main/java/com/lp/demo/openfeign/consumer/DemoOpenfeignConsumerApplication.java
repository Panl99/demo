package com.lp.demo.openfeign.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoOpenfeignConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOpenfeignConsumerApplication.class, args);
    }

}
