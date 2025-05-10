package com.lp.demo.langchain4j.example.handler;

import com.lp.demo.langchain4j.example.config.DeepSeekConfig;
import com.lp.demo.langchain4j.example.enums.LLMProviderEnum;
import com.lp.demo.langchain4j.example.request.ChatRequest;
import com.lp.demo.langchain4j.example.service.AssistantService;
import com.lp.demo.langchain4j.example.strategy.LLMStrategyService;
import com.lp.demo.langchain4j.example.strategy.StrategyFactory;
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
public class DeepSeekHandler extends LLMStrategyService {

    @Autowired
    StrategyFactory<LLMProviderEnum, LLMStrategyService> strategyFactory;
    @Resource
    DeepSeekConfig config;

    @Override
    public Object invoke(ChatRequest request) {
        log.info("DeepSeekHandler.invoke, request: {}", request);
        AssistantService assistantService = super.getAssistantService(config, request);
        if (request.getStream()) {
            return assistantService.streamingChat(request.getSessionId(), request.getPrompt());
        } else {
            return assistantService.chat(request);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(LLMProviderEnum.DEEPSEEK, this);
    }
}
