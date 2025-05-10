package com.lp.demo.langchain4j.example.request;

import com.lp.demo.langchain4j.example.enums.LLMProviderEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author lp
 * @date 2025/4/8 18:29
 * @desc
 **/
@Data
@Builder
public class ChatRequest {

    /**
     * 会话id
     * 可以是userId
     */
    private String sessionId;

    /**
     * 用户提示词
     */
    private String prompt;

    /**
     * 提供者
     * {@link LLMProviderEnum}
     */
    private String provider;

    /**
     * 是否流式响应
     */
    private Boolean stream;

}
