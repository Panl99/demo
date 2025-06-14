package com.lp.demo.langchain4j.example2.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * @author lp
 * @date 2025/5/14 9:23
 * @desc
 **/
public interface Assistant {

//    @SystemMessage("请以特朗普的语气来对话")
    String chat(String message);

    String chat(@MemoryId String memoryId, @UserMessage String message);

}
