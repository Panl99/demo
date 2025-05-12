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
public class OllamaHandler extends LLMStrategyService {

    @Autowired
    StrategyFactory<LLMProviderEnum, LLMStrategyService> strategyFactory;
    @Resource
    OllamaConfig ollamaConfig;

    @Override
    public Object invoke(ChatRequest request) {
        log.info("OllamaHandler.invoke, request: {}", request);
        if (request.getStream()) {
            OllamaStreamingChatModel streamingChatModel = OllamaStreamingChatModel.builder()
                    .baseUrl(ollamaConfig.getBaseUrl())
                    .modelName(ollamaConfig.getModel())
                    .build();
            return AiServices.builder(AssistantService.class)
                    .streamingChatModel(streamingChatModel)
                    .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                    .build()
                    .streamingChat(request.getSessionId(), request.getPrompt());
        } else {
            OllamaChatModel chatModel = OllamaChatModel.builder()
                    .baseUrl(ollamaConfig.getBaseUrl())
                    .modelName(ollamaConfig.getModel())
                    .build();
            return AiServices.builder(AssistantService.class)
                    .chatModel(chatModel)
                    .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                    .build()
                    .chat(request);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(LLMProviderEnum.LOCAL_DEEPSEEK, this);
        strategyFactory.register(LLMProviderEnum.LOCAL_QWEN, this);
    }
}
