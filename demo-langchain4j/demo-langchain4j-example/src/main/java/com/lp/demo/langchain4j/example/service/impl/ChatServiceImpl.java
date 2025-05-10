package com.lp.demo.langchain4j.example.service.impl;

import com.lp.demo.langchain4j.example.config.DeepSeekConfig;
import com.lp.demo.langchain4j.example.enums.LLMProviderEnum;
import com.lp.demo.langchain4j.example.request.ChatRequest;
import com.lp.demo.langchain4j.example.service.ChatService;
import com.lp.demo.langchain4j.example.strategy.LLMStrategyService;
import com.lp.demo.langchain4j.example.strategy.StrategyFactory;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author lp
 * @date 2025/4/8 18:26
 * @desc
 **/
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    StrategyFactory<LLMProviderEnum, LLMStrategyService> strategyFactory;

    @Override
    public String chat(ChatRequest request) {
        LLMProviderEnum providerEnum = LLMProviderEnum.of(request.getProvider()).orElse(LLMProviderEnum.LOCAL_DEEPSEEK);
        LLMStrategyService strategyService = strategyFactory.get(providerEnum);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .stream(false)
//                .user(request.getSessionId())
//                .model(deepSeekConfig.getModel())
//                .addUserMessage(request.getPrompt())
//                .build();
        return (String) strategyService.invoke(request);
    }

    @Override
    public Flux<String> streamingChat(ChatRequest request) {
        LLMProviderEnum providerEnum = LLMProviderEnum.of(request.getProvider()).orElse(LLMProviderEnum.LOCAL_DEEPSEEK);
        LLMStrategyService strategyService = strategyFactory.get(providerEnum);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .stream(true)
//                .user(request.getSessionId())
//                .model(deepSeekConfig.getModel())
//                .addUserMessage(request.getPrompt())
//                .build();
        return (Flux<String>) strategyService.invoke(request);
    }
}
