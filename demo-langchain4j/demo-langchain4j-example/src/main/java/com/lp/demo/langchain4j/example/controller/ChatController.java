package com.lp.demo.langchain4j.example.controller;

import com.lp.demo.langchain4j.example.request.ChatRequest;
import com.lp.demo.langchain4j.example.service.ChatService;
import com.lp.demo.langchain4j.example.util.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("")
public class ChatController {

    @Resource
    private ChatService chatService;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE + "; charset=UTF-8")
    public String chat(String prompt, String provider, HttpServletRequest request) {
        log.info("ip: {}", Utils.getClientIp(request));
        ChatRequest chatRequest = ChatRequest.builder()
                .prompt(prompt)
                .provider(provider)
                .sessionId(request.getSession().getId())
                .stream(false)
                .build();
        return chatService.chat(chatRequest);
    }

    @GetMapping(value = "/chat/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE + "; charset=UTF-8")
    public Flux<String> streamingChat(String prompt, String provider, HttpServletRequest request) {
        log.info("ip: {}", Utils.getClientIp(request));
        ChatRequest chatRequest = ChatRequest.builder()
                .prompt(prompt)
                .provider(provider)
                .sessionId(request.getSession().getId())
                .stream(true)
                .build();
        return chatService.streamingChat(chatRequest);
    }
}