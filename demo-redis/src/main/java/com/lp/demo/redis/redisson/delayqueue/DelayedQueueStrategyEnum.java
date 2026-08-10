package com.lp.demo.redis.redisson.delayqueue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lp.demo.common.result.BaseEnum;

import java.util.Optional;

/**
 * @author lp
 * @date 2025/10/13 11:33
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum DelayedQueueStrategyEnum implements BaseEnum<DelayedQueueStrategyEnum>, Strategy {
    /**
     * 设备告警延迟队列消费者
     */
    DEVICE_ALARM_DELAYED_QUEUE_CONSUMER(1, "设备告警延迟队列消费者", "iot:device:alarm:queue"),

    /**
     * 经纬度定时延迟队列消费者
     */
    LON_LAT_TIMING_QUEUE_CONSUMER(2, "经纬度定时延迟队列消费者", "iot:lonLatTiming:queue"),

    ;

    private final Integer code;
    private final String name;
    private final String queueName;

    public static Optional<DelayedQueueStrategyEnum> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(DelayedQueueStrategyEnum.class, code));
    }

    public static boolean contains(Integer code) {
        return of(code).isPresent();
    }

    public boolean match(Integer code) {
        return this.getCode().equals(code);
    }

}
