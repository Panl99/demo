//package com.lp.demo.langchain4j.example.handler;
//
//import com.lp.demo.langchain4j.example.config.QwenConfig;
//import com.lp.demo.langchain4j.example.enums.LLMProviderEnum;
//import com.lp.demo.langchain4j.example.request.ChatRequest;
//import com.lp.demo.langchain4j.example.service.AssistantService;
//import com.lp.demo.langchain4j.example.strategy.LLMStrategyService;
//import com.lp.demo.langchain4j.example.strategy.StrategyFactory;
//import dev.langchain4j.community.model.dashscope.QwenChatModel;
//import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
//import dev.langchain4j.memory.chat.MessageWindowChatMemory;
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import dev.langchain4j.service.AiServices;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author lp
// * @date 2025/4/8 18:17
// * @desc
// **/
//@Slf4j
//@Component
//public class QwenHandler extends LLMStrategyService {
//
//    @Autowired
//    StrategyFactory<LLMProviderEnum, LLMStrategyService> strategyFactory;
//    @Resource
//    QwenConfig qwenConfig;
//
//    @Override
//    public Object invoke(ChatRequest request) {
//        log.info("QwenHandler.invoke, request: {}", request);
//        if (request.getStream()) {
//            QwenStreamingChatModel streamingChatModel = QwenStreamingChatModel.builder()
//                    .baseUrl(qwenConfig.getBaseUrl())
//                    .modelName(qwenConfig.getModel())
//                    .build();
//            return AiServices.builder(AssistantService.class)
//                    .streamingChatLanguageModel(streamingChatModel)
//                    .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
//                    .build()
//                    .streamingChat(request.getSessionId(), request.getPrompt());
//        } else {
//            QwenChatModel chatModel = QwenChatModel.builder()
//                    .baseUrl(qwenConfig.getBaseUrl())
//                    .modelName(qwenConfig.getModel())
//                    .build();
//            return AiServices.builder(AssistantService.class)
//                    .chatLanguageModel(chatModel)
//                    .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
//                    .build()
//                    .chat(request);
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        strategyFactory.register(LLMProviderEnum.QWEN, this);
//    }
//}
