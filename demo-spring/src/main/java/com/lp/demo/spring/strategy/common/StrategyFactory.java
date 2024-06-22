package com.lp.demo.spring.strategy.common;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Lazy
@Component
public class StrategyFactory<K extends StrategyType, V extends StrategyService> {

    private final Map<K, V> STRATEGY_INSTANCE_MAP = new ConcurrentHashMap<>();

    public void register(K k, V v) {
        if (k != null && v != null) {
            STRATEGY_INSTANCE_MAP.put(k, v);
        }
    }

    public V get(K k) {
        return STRATEGY_INSTANCE_MAP.get(k);
    }

}
