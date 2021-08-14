package com.lp.demo.business;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableDubbo(scanBasePackages = "com.lp.demo.business")
public class DemoBusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBusinessServiceApplication.class, args);
    }

}
