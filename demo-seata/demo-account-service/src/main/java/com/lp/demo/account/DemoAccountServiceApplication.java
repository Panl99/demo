package com.lp.demo.account;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lp.demo.account")
@EnableDubbo(scanBasePackages = "com.lp.demo.account")
public class DemoAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAccountServiceApplication.class, args);
    }

}
