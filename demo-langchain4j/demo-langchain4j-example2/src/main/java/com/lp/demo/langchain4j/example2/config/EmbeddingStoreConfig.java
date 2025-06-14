package com.lp.demo.langchain4j.example2.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmbeddingStoreConfig {

    final PgEmbeddingStoreProperties pgConfig;

//    final RedisEmbeddingStoreProperties redisEmbeddingStoreProperties;

//    /**
//     * 向量存储：内存
//     * @return
//     */
//    @Bean
//    public EmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
//        return new InMemoryEmbeddingStore<>();
//    }

//    /**
//     * 使用redis存储向量
//     *
//     * 注意：
//     * 1、redis服务器版本>4.0，才支持模块系统：RedisSearch模块
//     * 2、使用redis作为embedding store需要确保redis安装了redis search模块，否则启动会报：ERR unknown command 'FT._LIST'
//     * 3、MODULE LIST：查看加载模块列表；INFO MODULES：检查模块系统状态
//     * 4、使用 Redis Stack（集成 RediSearch、RedisJSON 等模块）
//     *
//     * @return
//     */
//    @Bean
//    public EmbeddingStore<TextSegment> redisEmbeddingStore() {
//        return RedisEmbeddingStore.builder()
//                .host(redisEmbeddingStoreProperties.getHost())
//                .port(redisEmbeddingStoreProperties.getPort())
//                .dimension(redisEmbeddingStoreProperties.getDimension())
//                .build();
//    }


    /**
     * 使用postgres存储向量
     *
     * 1、安装postgres
     *
     * @return
     */
    @Bean
    public EmbeddingStore<TextSegment> pgVectorEmbeddingStore() {
        return PgVectorEmbeddingStore.builder()
            .table(pgConfig.getTable())
            .dropTableFirst(true)
            .createTable(true)
            .host(pgConfig.getHost())
            .port(pgConfig.getPort())
            .user(pgConfig.getUser())
            .password(pgConfig.getPassword())
            .dimension(384)
            .database(pgConfig.getDatabase())
            .build();
    }

}
