package com.lp.demo.ai.config.deepseek;

import com.lp.demo.ai.core.LogLevel;
import com.lp.demo.ai.core.chat.ChatCompletionModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.Proxy;

@Data
@ConfigurationProperties(prefix = "ollama")
public class OllamaProperties {

    /**
     * 基本 URL
     */
    private String baseUrl = "http://127.0.0.1:11434/v1";

    /**
     * API 密钥
     */
    private String apiKey;

    /**
     * 搜索 API 密钥
     */
    private String searchApiKey;

    /**
     * 模型名称
     */
    private String model = ChatCompletionModel.DEEPSEEK_R1_7B.getValue();

    /**
     * 默认系统提示词
     */
    private boolean defaultSystemPrompt = true;

    /**
     * 日志请求
     */
    private boolean logRequests;

    /**
     * 日志响应
     */
    private boolean logResponses;

    /**
     * 代理
     */
    private Proxy proxy;

    /**
     * 连接超时 S
     */
    private Integer connectTimeout;

    /**
     * 读取超时 S
     */
    private Integer readTimeout;

    /**
     * 呼叫超时 S
     */
    private Integer callTimeout;

    /**
     * 日志级别
     */
    private LogLevel logLevel = LogLevel.DEBUG;

}
