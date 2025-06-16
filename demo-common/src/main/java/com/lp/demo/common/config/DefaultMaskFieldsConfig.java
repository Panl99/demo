package com.lp.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @date 2025/6/16 15:03
 * @desc 日志记录中默认需要掩码的字段配置
 **/
@Configuration(proxyBeanMethods = false)
public class DefaultMaskFieldsConfig {

    /**
     * 部分掩码
     */
    @Bean
    public String[] getMaskPartFields() {
        return new String[]{
                "phone",
                "email",
                "ip",
                "ipAddress",
                "clientIp",
                "remoteAddress",
        };
    }

    /**
     * 全部掩码
     */
    @Bean
    public String[] getMaskAllFields() {
        return new String[]{
                "password",
                "pwd",
                "secret",
        };
    }

}
