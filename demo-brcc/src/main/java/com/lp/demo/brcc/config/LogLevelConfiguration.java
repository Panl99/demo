package com.lp.demo.brcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @date 2021/7/1 12:11
 *
 **/
@Configuration
public class LogLevelConfiguration {
    @Value("${root}")
    private String rootLogLevel;
    @Value("${com.lp.demo.rcc}")
    private String rccLogLevel;

//    enum LogLevel {
//        @Value("${root}")
//        LOG_LEVEL_ROOT("root"),
//        @Value("${com.lp.demo.rcc}")
//        LOG_LEVEL_RCC("com.lp.demo.rcc");
//
//        private String logLevelName;
//
//        LogLevel(String logLevelName) {
//            this.logLevelName = logLevelName;
//        }
//
//        public String getLogLevelName() {
//            return logLevelName;
//        }
//    }

//    @Subscribe
//    public void onChanged(List newValue){
//        System.out.println("LogLevelConfiguration changed:"+ newValue);
//    }

}
