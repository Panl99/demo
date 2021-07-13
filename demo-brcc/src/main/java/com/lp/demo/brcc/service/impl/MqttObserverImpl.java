package com.lp.demo.brcc.service.impl;

import com.lp.demo.brcc.service.Observer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2021/7/3 10:23
 **/
@Service
@Slf4j
public class MqttObserverImpl implements Observer {

    @Override
    public void notify(Object obj) {
        log.info("receive mqttConfig notify: {}", obj);
    }
}
