package com.lp.demo.common.config;

import com.lp.demo.common.interceptor.OperatorLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lp
 * @date 2022/4/21 11:17
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 过滤掉登录接口
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OperatorLogInterceptor()).addPathPatterns("/**").excludePathPatterns("/api/login");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
