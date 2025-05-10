package com.lp.demo.langchain4j.example.service;

import com.lp.demo.langchain4j.example.request.ChatRequest;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * @author lp
 * @date 2025/4/8 18:26
 * @desc
 **/
public interface AssistantService {

    String chat(ChatRequest request);

    Flux<String> streamingChat(@MemoryId String sessionId, @UserMessage String message);

}
