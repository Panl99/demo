package com.lp.demo.ai.config.deepseek;

import com.lp.demo.ai.core.DeepSeekConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "embedding")
public class EmbeddingProperties extends DeepSeekConfig {

	/**
	 * 基本 URL
	 */
	private String baseUrl = "http://127.0.0.1:11434/v1";

}
