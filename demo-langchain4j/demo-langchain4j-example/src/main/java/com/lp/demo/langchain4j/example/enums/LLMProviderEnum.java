package com.lp.demo.langchain4j.example.enums;

import com.lp.demo.langchain4j.example.strategy.Strategy;

import java.util.Optional;

/**
 * @author lp
 * @date 2021/9/8 11:36
 **/
public enum LLMProviderEnum implements BaseEnum<LLMProviderEnum>, Strategy {
    /**
     * deepseek
     */
    DEEPSEEK("deepseek", "深度求索"),
    /**
     * ollama
     */
    LOCAL_DEEPSEEK("ollama_deepseek", "本地DeepSeek"),
    LOCAL_QWEN("ollama_qwen", "本地Qwen"),

    /**
     * qwen
     */
    QWEN("qwen", "千问"),

    ;

    private final String code;
    private final String name;

    LLMProviderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Optional<LLMProviderEnum> of(String code) {
        return Optional.ofNullable(BaseEnum.getByCode(LLMProviderEnum.class, code));
    }
}
