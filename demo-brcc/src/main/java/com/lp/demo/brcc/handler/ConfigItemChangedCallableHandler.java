package com.lp.demo.brcc.handler;

import com.baidu.brcc.ConfigItemChangedCallable;
import com.baidu.brcc.model.ChangedConfigItem;
import com.lp.demo.brcc.service.impl.Feed;
import com.lp.demo.brcc.service.impl.LogLevelObserverImpl;
import com.lp.demo.brcc.service.impl.MqttObserverImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lp
 * @date 2021/6/8 17:58
 **/
@Service
@Slf4j
public class ConfigItemChangedCallableHandler implements ConfigItemChangedCallable {

//    @Autowired
//    private EventBus eventBus;

    @Autowired
    Feed feed;

    @Override
    public void changed(List<ChangedConfigItem> items) {
        if (!CollectionUtils.isEmpty(items)) {
//            eventBus.post(items);

            feed.notifyObservers(items);

//            Iterator it = items.iterator();
//
//            while(it.hasNext()) {
//                ChangedConfigItem item = (ChangedConfigItem)it.next();
//                log.info("changed key={} oldValue={} newValue={}", item.getKey(), item.getOldValue(), item.getNewValue());
//
//            }
        }
    }

    @PostConstruct
    public void register() {
//        eventBus.register(new ConfigServiceImpl());

        feed.registerObserver(new LogLevelObserverImpl());
        feed.registerObserver(new MqttObserverImpl());
    }

//    @Bean
//    public Feed getFeed() {
//        return new Feed();
//    }
}
