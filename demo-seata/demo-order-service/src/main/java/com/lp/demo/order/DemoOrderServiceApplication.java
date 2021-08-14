package com.lp.demo.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lp.demo.order")
@EnableDubbo(scanBasePackages = "com.lp.demo.order")
public class DemoOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOrderServiceApplication.class, args);
    }

}
