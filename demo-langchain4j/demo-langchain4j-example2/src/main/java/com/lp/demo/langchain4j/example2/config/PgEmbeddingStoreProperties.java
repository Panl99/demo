package com.lp.demo.langchain4j.example2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pgvector")
@Data
public class PgEmbeddingStoreProperties {

    private String host;

    private int port;

    private String database;

    private String user;

    private String password;

    private String table;

}