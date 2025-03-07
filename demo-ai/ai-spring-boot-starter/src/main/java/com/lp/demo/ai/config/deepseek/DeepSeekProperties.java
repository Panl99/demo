package com.lp.demo.ai.config.deepseek;

import com.lp.demo.ai.core.DeepSeekConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekProperties extends DeepSeekConfig {

}
