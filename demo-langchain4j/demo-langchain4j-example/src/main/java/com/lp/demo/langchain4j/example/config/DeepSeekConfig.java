package com.lp.demo.langchain4j.example.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @date 2025/4/8 17:07
 * @desc
 **/
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "deepseek")
@Data
public class DeepSeekConfig extends OpenAiConfig {

}
