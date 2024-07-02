package com.lp.demo.spring.event.another;

import org.springframework.context.ApplicationEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lp
 * @date 2023/8/23 11:33
 * @desc 抽象事件类
 **/
public class Event extends ApplicationEvent {

    private static final long serialVersionUID = -7350407582134152264L;

    private static final AtomicInteger AI = new AtomicInteger();

    private final int sn;

    public Event(Object source) {
        super(source);
        this.sn = AI.incrementAndGet();
    }

    public int getSn() {
        return sn;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", sn=" + sn +
                ", source=" + source +
                '}';
    }
}
