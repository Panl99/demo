package com.lp.demo.spring.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @desc
 **/
@Configuration
public class StrategyHandlerConfig {

    /**
     * 配置StrategyHandler_A1具体处理器
     * @return
     */
    @Bean
    public StrategyHandler_A1 initSubHandler() {
        // 指定处理handler
        return new StrategyHandler_A1_sub1();
    }
}
