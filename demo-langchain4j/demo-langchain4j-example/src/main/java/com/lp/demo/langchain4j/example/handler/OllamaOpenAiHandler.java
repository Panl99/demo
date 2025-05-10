package com.lp.demo.langchain4j.example.handler;

import com.lp.demo.langchain4j.example.config.OllamaConfig;
import com.lp.demo.langchain4j.example.enums.LLMProviderEnum;
import com.lp.demo.langchain4j.example.request.ChatRequest;
import com.lp.demo.langchain4j.example.service.AssistantService;
import com.lp.demo.langchain4j.example.strategy.LLMStrategyService;
import com.lp.demo.langchain4j.example.strategy.StrategyFactory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2025/4/8 18:17
 * @desc
 **/
@Slf4j
@Component
public class OllamaOpenAiHandler extends LLMStrategyService {

    @Autowired
    StrategyFactory<LLMProviderEnum, LLMStrategyService> strategyFactory;
    @Resource
    OllamaConfig config;

    @Override
    public Object invoke(ChatRequest request) {
        log.info("OllamaOpenAiHandler.invoke, request: {}", request);
        config.setBaseUrl(config.getBaseUrl().endsWith("/v1") ? config.getBaseUrl() : config.getBaseUrl() + "/v1");
        AssistantService assistantService = super.getAssistantService(config, request);
        if (request.getStream()) {
            return assistantService.streamingChat(request.getSessionId(), request.getPrompt());
        } else {
            return assistantService.chat(request);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(LLMProviderEnum.LOCAL_DEEPSEEK, this);
        strategyFactory.register(LLMProviderEnum.LOCAL_QWEN, this);
    }
}
