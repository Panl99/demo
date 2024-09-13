package com.lp.demo.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 */
@Configuration
@ConfigurationProperties(prefix = "secret")
@Data
public class SecretConfig {

    private static final String DEFAULT_KEY = "6dmt1yWK2Jze8M375Xb7h6E11xftDXHa";

    private String encryptKey = DEFAULT_KEY;
    private String decryptKey = DEFAULT_KEY;
}
