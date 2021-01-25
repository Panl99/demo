package com.outman.demomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication//(exclude= {DataSourceAutoConfiguration.class})
public class DemoMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoMainApplication.class, args);
    }

}
