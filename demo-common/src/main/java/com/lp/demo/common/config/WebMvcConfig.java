package com.lp.demo.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.demo.common.interceptor.OperatorLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lp
 * @date 2022/4/21 11:17
 **/
@Configuration
public class WebMvcConfig implements /*WebMvcConfigurationSupport*/WebMvcConfigurer {
    /**
     * 过滤掉登录接口
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OperatorLogInterceptor()).addPathPatterns("/**").excludePathPatterns("/api/login");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

//    /**
//     * 设置静态路径
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
//        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
//        registry.addResourceHandler(("/**")).addResourceLocations("file:" + "#uploadPath");
//        // Swagger
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        super.addResourceHandlers(registry);
//    }
//
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//        // 反序列化忽略实体类没有的属性
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        jsonConverter.setObjectMapper(objectMapper);
//        converters.add(jsonConverter);
//        super.addDefaultHttpMessageConverters(converters);
//    }

//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 移除StringHttpMessageConverter，解决ResponseAdvice统一处理返回时，返回值为字符串类型的转换报错
//        converters.removeIf(x -> x instanceof StringHttpMessageConverter);
//    }
}
