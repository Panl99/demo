package com.lp.demo.shardingsphere.jdbc.server;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan("com.lp.demo.shardingsphere.jdbc.domain.mapper")
@SpringBootApplication(scanBasePackages = {"com.lp.demo.shardingsphere.jdbc"})
public class DemoShardingsphereJdbcServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoShardingsphereJdbcServerApplication.class);

    public static void main(String[] args) {
        //dubbo 默认日志走log4j 启动做一点调整
        System.setProperty("dubbo.application.logger", "slf4j");
        SpringApplication.run(DemoShardingsphereJdbcServerApplication.class, args);
        logger.info("        >>>>>>>>服务启动成功<<<<<<<<        ");
    }

}
