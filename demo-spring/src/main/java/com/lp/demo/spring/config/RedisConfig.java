package com.lp.demo.spring.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //对象的序列化
        RedisSerializationContext.SerializationPair valueSerializationPair
                = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer());
        //全局redis缓存过期时间
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24L))
                .serializeValuesWith(valueSerializationPair);

        return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(factory), redisCacheConfiguration);
    }

    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        return jackson2JsonRedisSerializer;
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    /**
     * redis消息监听器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory/*,
                                                                       MessageListener messageListener*/) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 集成spring的KeyExpirationEventMessageListener，不自己注册监听器
        // 监听所有key过期事件(*代表所有数据库) redis.conf配置：notify-keyspace-events "Ex"
//        container.addMessageListener(messageListener, new PatternTopic("__keyevent@*__:expired"));
//        container.addMessageListener(messageListener, new PatternTopic("__keyevent@*__:del"));
        // 监听某个具体的key，redis.conf配置：notify-keyspace-events "Kx"
//        container.addMessageListener(messageListener, new PatternTopic("__keyspace@*__:"+ "具体key"));

        return container;
    }

}
