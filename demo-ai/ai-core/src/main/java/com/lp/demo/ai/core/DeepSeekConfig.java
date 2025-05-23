package com.lp.demo.ai.core;

import com.lp.demo.ai.core.LogLevel;
import com.lp.demo.ai.core.chat.ChatCompletionModel;
import lombok.Data;

import java.net.Proxy;


@Data
public class DeepSeekConfig {

	/**
	 * 基本 URL
	 */
	protected String baseUrl = "https://api.deepseek.com/v1";

	/**
	 * API 密钥
	 */
	protected String apiKey;

	/**
	 * 搜索 API 密钥
	 */
	protected String searchApiKey;

	/**
	 * 模型名称
	 */
	protected String model = ChatCompletionModel.DEEPSEEK_REASONER.getValue();

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
	protected Integer connectTimeout;

	/**
	 * 读取超时 S
	 */
	protected Integer readTimeout;

	/**
	 * 呼叫超时 S
	 */
	protected Integer callTimeout;

	/**
	 * 日志级别
	 */
	protected LogLevel logLevel = LogLevel.DEBUG;

}
