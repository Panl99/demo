package com.lp.demo.storage;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lp.demo.storage")
@EnableDubbo(scanBasePackages = "com.lp.demo.storage")
public class DemoStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoStorageServiceApplication.class, args);
    }

}
