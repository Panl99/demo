package com.lp.demo.common.service;

import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2021/10/8 12:16
 **/
@Component
public class ThreadLocalService<T> {

    private final ThreadLocal<T> threadLocal;

    private ThreadLocalService() {
        threadLocal = new ThreadLocal<>();
    }

    public T getValue() {
        return threadLocal.get();
    }

    public void setValue(T t) {
        threadLocal.set(t);
    }

    public void remove() {
        threadLocal.remove();
    }

    public T getAndRemove() {
        T t = threadLocal.get();
        if (t != null) {
            remove();
        }
        return t;
    }

    private static ThreadLocalService threadLocalService = new ThreadLocalService<>();

    public static <T> ThreadLocalService<T> getInstance() {
        return threadLocalService;
    }
}
