package com.lp.demo.action.spring_in_action.debounce.config;

import com.lp.demo.action.spring_in_action.debounce.keyparser.DefaultDebounceKeyParser;
import com.lp.demo.action.spring_in_action.debounce.keyparser.ExpressionDebounceKeyParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
public class DebounceConfiguration {

    @Bean
    public DefaultDebounceKeyParser defaultDebounceKeyParser() {
        return new DefaultDebounceKeyParser();
    }

    @Bean
    public ExpressionDebounceKeyParser expressionDebounceKeyParser() {
        return new ExpressionDebounceKeyParser();
    }

}
