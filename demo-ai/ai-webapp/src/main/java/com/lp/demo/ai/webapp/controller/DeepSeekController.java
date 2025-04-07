package com.lp.demo.ai.webapp.controller;

import com.lp.demo.ai.core.DeepSeekClient;
import com.lp.demo.ai.core.chat.ChatCompletionResponse;
import com.lp.demo.ai.core.models.ModelsResponse;
import com.lp.demo.ai.webapp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
public class DeepSeekController {

	@Resource
	private DeepSeekClient deepSeekClient;

	@GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE + "; charset=UTF-8")
	public Flux<ChatCompletionResponse> chat(String prompt, HttpServletRequest request) {
		log.info("ip: {}", Utils.getClientIp(request));
		return deepSeekClient.chatFluxCompletion(prompt);
	}

	@GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelsResponse models() {
		return deepSeekClient.models();
	}


}
