package com.lp.demo.langchain4j.example2.config;

import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebSearchConfig {

    final SearchConfigProperties searchConfig;


    @Bean
    public SearchApiWebSearchEngine initWebSearchEngine() {
        return SearchApiWebSearchEngine.builder()
            .engine(searchConfig.getEngine())
            .apiKey(searchConfig.getApiKey())
            .build();
    }
}
