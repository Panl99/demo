package com.lp.demo.action.spring_in_action.debounce.aop;

import com.lp.demo.action.spring_in_action.debounce.annotation.Debounce;
import com.lp.demo.action.spring_in_action.debounce.keyparser.DebounceKeyParser;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.ResultEnum;
import com.lp.demo.common.service.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author lp
 */
@Slf4j
@Aspect
@Component
public class DebounceAspect {

    private static final String CACHE_KEY = "debounce";
    private final Map<Class<? extends DebounceKeyParser>, DebounceKeyParser> keyParserMap;
    private final ThreadLocalService<Map<String, String>> threadLocalService = ThreadLocalService.getInstance();

    @Autowired
    private final RedissonClient redissonClient;

    public DebounceAspect(List<DebounceKeyParser> keyParser, RedissonClient redissonClient) {
        this.keyParserMap = keyParser.stream().collect(Collectors.toMap(DebounceKeyParser::getClass, Function.identity(), (v1, v2) -> v1, HashMap::new));
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(com.lp.demo.action.spring_in_action.debounce.annotation.Debounce)")
    public void debounce() {
    }

    @Before("@annotation(debounce)")
    public void beforePointCut(JoinPoint joinPoint, Debounce debounce) {
        // 获取key解析器
        DebounceKeyParser keyParser = keyParserMap.get(debounce.keyParser());
        if (keyParser == null) {
            throw new DisplayableException(ResultEnum.FAIL.getCode(), "key parser not found!");
        }
        // 解析获得key
        String key = keyParser.getKey(joinPoint, debounce);
        // 锁定key
        RMapCache<String, Object> mapCache = redissonClient.getMapCache(CACHE_KEY);
        if (mapCache.get(key) != null) {
            throw new DisplayableException(ResultEnum.FAIL.getCode(), debounce.message());
        }
        synchronized (this) {
            String value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            Object o = mapCache.putIfAbsent(key, value, debounce.timeout(), debounce.timeUnit());
            if (o != null) {
                throw new DisplayableException(ResultEnum.FAIL.getCode(), debounce.message());
            }
        }
        Map<String, String> map = CollectionUtils.isEmpty(threadLocalService.getValue()) ? new HashMap<>() : threadLocalService.getValue();
        map.put(CACHE_KEY, key);
        threadLocalService.setValue(map);
    }

    @After("@annotation(debounce)")
    public void afterPointCut(JoinPoint joinPoint, Debounce debounce) {
        Map<String, String> map = threadLocalService.getAndRemove();
        if(CollectionUtils.isEmpty(map)){
            return;
        }
        if (debounce.delKey()) {
            RMapCache<String, Object> mapCache = redissonClient.getMapCache(CACHE_KEY);
            if (mapCache.size() == 0) {
                return;
            }
            String key = map.get(CACHE_KEY);
            mapCache.fastRemove(key);
        }
    }

}
