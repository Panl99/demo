package com.lp.demo.spring.event.another;

import org.springframework.context.ApplicationEvent;

/**
 * @author lp
 * @date 2023/8/23 11:33
 * @desc 抽象事件类
 **/
public class Event<T> extends ApplicationEvent {

    private static final long serialVersionUID = -7350407582134152264L;
    /**
     * 系统类型
     */
    private EventTypeEnum eventType;

    private T data;

    public Event(Object source, EventTypeEnum eventType, T data) {
        super(source);
        this.eventType = eventType;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Event<T> setData(T data) {
        this.data = data;
        return this;
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", data=" + data +
                ", source=" + source +
                '}';
    }
}
