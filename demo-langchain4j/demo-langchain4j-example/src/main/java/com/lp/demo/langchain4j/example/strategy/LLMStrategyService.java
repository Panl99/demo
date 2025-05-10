package com.lp.demo.langchain4j.example.strategy;


import com.lp.demo.langchain4j.example.config.OpenAiConfig;
import com.lp.demo.langchain4j.example.request.ChatRequest;
import com.lp.demo.langchain4j.example.service.AssistantService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.service.AiServices;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LLMStrategyService implements StrategyService {

    private static final Map<String, AssistantService> ASSISTANT_SERVICE_SESSION_MAP = new ConcurrentHashMap<>();

    public AssistantService getAssistantService(OpenAiConfig config, ChatRequest request) {
        if (ASSISTANT_SERVICE_SESSION_MAP.containsKey(request.getSessionId())) {
            return ASSISTANT_SERVICE_SESSION_MAP.get(request.getSessionId());
        }
        OpenAiStreamingChatModel streamingChatModel = OpenAiStreamingChatModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(config.getModel())
                .build();
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(config.getModel())
                .build();
        AssistantService assistantService = AiServices.builder(AssistantService.class)
                .chatLanguageModel(chatModel)
                .streamingChatLanguageModel(streamingChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
        ASSISTANT_SERVICE_SESSION_MAP.put(request.getSessionId(), assistantService);
        return assistantService;
    }

    public void invoke() {
        throw new UnsupportedOperationException();
    }

    public Object invoke(ChatRequest request) {
        throw new UnsupportedOperationException();
    }

    public ChatCompletionResponse openAiChat(ChatCompletionRequest request) {
        throw new UnsupportedOperationException();
    }

}
