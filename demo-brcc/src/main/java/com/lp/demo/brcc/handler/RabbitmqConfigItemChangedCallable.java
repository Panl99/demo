package com.lp.demo.brcc.handler;

import com.baidu.brcc.ConfigItemChangedCallable;
import com.baidu.brcc.model.ChangedConfigItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lp
 * @date 2021/6/9 22:54
 * @description
 **/
@Slf4j
public class RabbitmqConfigItemChangedCallable implements ConfigItemChangedCallable {

    @Override
    public void changed(List<ChangedConfigItem> items) {
        if (!CollectionUtils.isEmpty(items)) {
            for (ChangedConfigItem item : items) {
                log.info("changed key={} oldValue={} newValue={}", item.getKey(), item.getOldValue(), item.getNewValue());
            }
        }
    }
}
