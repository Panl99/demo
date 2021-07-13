package com.lp.demo.brcc.config;

import com.baidu.brcc.ConfigItemChangedCallable;
import com.lp.demo.brcc.handler.ConfigItemChangedCallableHandler;
import com.lp.demo.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * @author lp
 * @date 2021/5/28 18:26
 **/
//@Component
@Slf4j
@Configuration
public class InitConfig implements CommandLineRunner {
//    @Autowired
//    MqttConfiguration mqttConfiguration;
//    @Autowired
//    LogLevelConfiguration logLevelConfiguration;

    @Autowired
    Environment env;

    @Autowired
    LoggingSystem loggingSystem;

    @Override
    public void run(String... args) throws Exception {
//        log.info("(InitConfig)Running default command line with: " + Arrays.asList(args));

        // 本地配置去更新配置中心配置
        // init system env
        log.info("Env:{}", env);

//        ApplicationContext context = new AnnotationConfigApplicationContext(InitConfig.class);


        // init mqtt config
        initMqttConfig();

        // init LogLevel, default info, get rcc and update
        initLogLevel();


    }

    /**
     * 初始化mqtt配置
     */
    private void initMqttConfig() {
//        MqttConfiguration mqttConfiguration = context.getBean(MqttConfiguration.class);
        Map mqttConfigValueMap = ObjectUtil.getAllFieldValueByName(getMqttConfiguration());
        Properties properties = new Properties();
        log.info("load mqtt properties from rcc, the value is: {}", mqttConfigValueMap);


    }

    /**
     * 初始化日志级别
     */
    private void initLogLevel() {
        Map logConfigValueMap = ObjectUtil.getAllFieldValueByName(getLogLevelConfiguration());
        log.info("load logLevel properties from rcc, the value is: {}", logConfigValueMap);

//        LoggerConfiguration logConfigRoot = loggingSystem.getLoggerConfiguration(LoggingSystem.ROOT_LOGGER_NAME);
//        LoggerConfiguration logConfigCurrent = loggingSystem.getLoggerConfiguration(LoggingSystem.SYSTEM_PROPERTY);
//        log.info("logConfigRoot = {}, logConfigCurrent = {}", logConfigRoot, logConfigCurrent);
//
//        logConfigValueMap.forEach((k, v) -> loggingSystem.setLogLevel(k.toString(), LogLevel.valueOf(String.valueOf(v).toUpperCase())));

        String rccLogLevel = String.valueOf(logConfigValueMap.get("rccLogLevel")).toUpperCase();
        LogLevel logLevel = LogLevel.valueOf(rccLogLevel);
        loggingSystem.setLogLevel("com.lp.demo.rcc", logLevel);
    }

    @Bean
    public ConfigItemChangedCallable configItemChangedCallable() {
        return new ConfigItemChangedCallableHandler();
    }

    @Bean
    public MqttConfiguration getMqttConfiguration() {
        return new MqttConfiguration();
    }

    @Bean
    public LogLevelConfiguration getLogLevelConfiguration() {
        return new LogLevelConfiguration();
    }

}
