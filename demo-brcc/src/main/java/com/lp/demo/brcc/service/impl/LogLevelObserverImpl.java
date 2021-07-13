package com.lp.demo.brcc.service.impl;

import com.baidu.brcc.model.ChangedConfigItem;
import com.lp.demo.brcc.service.Observer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lp
 * @date 2021/7/3 10:23
 **/
@Service
@Slf4j
public class LogLevelObserverImpl implements Observer {

    @Autowired
    LoggingSystem loggingSystem;

    @Override
    public void notify(Object obj) {
        log.info("receive logLevelConfig notify: {}", obj);

        if (obj instanceof List) {
            ((List<ChangedConfigItem>) obj).forEach(item -> {
                String key = item.getKey();
                String oldValue = item.getOldValue();
                String newValue = item.getNewValue();

                log.info("logLevel onchanged, key = {}, oldValue = {}, newValue = {}", key, oldValue, newValue);
                LogLevel logLevel = LogLevel.valueOf(newValue.toUpperCase());
                loggingSystem.setLogLevel(key, logLevel);
            });
        } else {
            log.warn("logLevel notify param is not List!");
        }

        log.info("logLevel notify Processing complete.");
    }

}
