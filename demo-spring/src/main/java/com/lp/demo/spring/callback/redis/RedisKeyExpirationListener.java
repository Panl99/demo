package com.lp.demo.spring.callback.redis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public abstract class RedisKeyExpirationListener extends KeyExpirationEventMessageListener /*implements MessageListener*/ {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    // 其它需要具体业务去做的方法

}