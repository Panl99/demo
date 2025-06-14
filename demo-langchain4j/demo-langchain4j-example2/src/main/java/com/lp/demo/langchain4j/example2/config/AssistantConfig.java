package com.lp.demo.langchain4j.example2.config;

import com.lp.demo.langchain4j.example2.functioncalling.HighLevelCalculator;
import com.lp.demo.langchain4j.example2.service.Assistant;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lp
 * @date 2025/5/14 9:47
 * @desc
 **/
@Configuration
@RequiredArgsConstructor
public class AssistantConfig {

    final ChatModel chatModel;

    @Bean
    public Assistant init(EmbeddingStore<TextSegment> embeddingStore, SearchApiWebSearchEngine engine) {
        return AiServices.builder(Assistant.class)
                // 记忆10条聊天记录，缺点：不考虑token长度，可能超限；对话历史短，易出现上下文断层。
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) // TokenWindowChatMemory.withMaxTokens(800)
                // 内容检索器：检索向量存储内容
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                // 工具调用
                .tools(new HighLevelCalculator(), new WebSearchTool(engine))
                // 聊天模型
                .chatModel(chatModel)
                .build();
    }

}
