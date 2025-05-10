package com.lp.demo.langchain4j.example.service;

import com.lp.demo.langchain4j.example.request.ChatRequest;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import reactor.core.publisher.Flux;

/**
 * @author lp
 * @date 2025/4/8 18:26
 * @desc
 **/
public interface ChatService {

    String chat(ChatRequest request);

    Flux<String> streamingChat(ChatRequest request);

}
