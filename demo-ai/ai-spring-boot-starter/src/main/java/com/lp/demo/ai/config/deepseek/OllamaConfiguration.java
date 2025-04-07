package com.lp.demo.ai.config.deepseek;

import com.lp.demo.ai.core.OllamaClient;
import com.lp.demo.ai.core.OpenAiClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OllamaProperties.class})
public class OllamaConfiguration {

	@Value("classpath:/prompts/system.pt")
	private Resource systemResource;

	/**
	 * Deep Seek 客户端
	 * @param ollamaProperties Deep Seek 属性
	 * @return {@link OpenAiClient }
	 */
	@SneakyThrows
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ollama", name = "api-key")
	public OllamaClient deepSeekLocalClient(OllamaProperties ollamaProperties) {

		OllamaClient.Builder builder = OllamaClient.builder().baseUrl(ollamaProperties.getBaseUrl())
				.model(ollamaProperties.getModel()).openAiApiKey(ollamaProperties.getApiKey())
				.logRequests(ollamaProperties.isLogRequests()).logResponses(ollamaProperties.isLogResponses());

		if (Objects.nonNull(ollamaProperties.getProxy())) {
			builder.proxy(ollamaProperties.getProxy());
		}

		if (Objects.nonNull(ollamaProperties.getConnectTimeout())) {
			builder.connectTimeout(Duration.ofSeconds(ollamaProperties.getConnectTimeout()));
		}

		if (Objects.nonNull(ollamaProperties.getReadTimeout())) {
			builder.readTimeout(Duration.ofSeconds(ollamaProperties.getReadTimeout()));
		}

		if (Objects.nonNull(ollamaProperties.getCallTimeout())) {
			builder.callTimeout(Duration.ofSeconds(ollamaProperties.getCallTimeout()));
		}

		builder.logLevel(ollamaProperties.getLogLevel());

		// 注入R1 提示词
		if (ollamaProperties.isDefaultSystemPrompt()) {
			String systemMessage = StreamUtils.copyToString(systemResource.getInputStream(), StandardCharsets.UTF_8);
			builder.systemMessage(systemMessage);
		}

		builder.searchApiKey(ollamaProperties.getSearchApiKey());
		return builder.build();
	}

}
