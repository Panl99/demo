package com.lp.demo.langchain4j.example.config;

import lombok.Data;

import java.net.Proxy;
import java.time.Duration;

/**
 * @author lp
 * @date 2025/4/8 17:07
 * @desc
 **/
@Data
public class OpenAiConfig {

    /**
     * 基本 URL
     */
    protected String baseUrl;

    /**
     * API 密钥
     */
    protected String apiKey;

    /**
     * 模型名称
     */
    protected String model;

    /**
     * 默认系统提示词
     */
    protected boolean defaultSystemPrompt = true;

    /**
     * 日志请求
     */
    protected boolean logRequests;

    /**
     * 日志响应
     */
    protected boolean logResponses;

    /**
     * 代理
     */
    protected Proxy proxy;

    /**
     * 连接超时 S
     */
    protected Duration connectTimeout = Duration.ofSeconds(60);

    /**
     * 读取超时 S
     */
    protected Duration readTimeout = Duration.ofSeconds(60);

    /**
     * 呼叫超时 S
     */
    protected Duration callTimeout = Duration.ofSeconds(60);

    /**
     * 写入超时 S
     */
    public Duration writeTimeout = Duration.ofSeconds(60);


}
