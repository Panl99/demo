package com.lp.demo.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2022/10/13 15:37
 * @desc
 **/
@Component
public abstract class Event<T> extends ApplicationContextEvent {
    private static final long serialVersionUID = -4392045849956671608L;

    private T data;

    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public Event(ApplicationContext source) {
        super(source);
    }

    public void execute(T data) {

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
