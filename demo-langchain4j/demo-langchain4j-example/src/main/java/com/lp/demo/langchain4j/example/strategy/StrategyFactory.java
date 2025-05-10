package com.lp.demo.langchain4j.example.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lp
 * @date 2023/8/29 18:55
 * @desc
 **/
@Slf4j
@Lazy
@Component("langchain4j.example.StrategyFactory")
public class StrategyFactory<K extends Strategy, V extends StrategyService> implements CommandLineRunner {

    private final Map<K, V> STRATEGY_INSTANCE_MAP = new ConcurrentHashMap<>();

    public void register(K k, V v) {
        if (k != null && v != null) {
            STRATEGY_INSTANCE_MAP.put(k, v);
        }
    }

    public V get(K k) {
        return STRATEGY_INSTANCE_MAP.get(k);
    }

    public Map<K, V> list() {
        return STRATEGY_INSTANCE_MAP;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("StrategyFactory list: {}", this.list());
    }
}
