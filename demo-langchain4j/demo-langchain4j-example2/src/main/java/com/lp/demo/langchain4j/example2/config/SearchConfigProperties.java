package com.lp.demo.langchain4j.example2.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "search")
@Data
public class SearchConfigProperties {

    private String engine;

    private String apiKey;

}