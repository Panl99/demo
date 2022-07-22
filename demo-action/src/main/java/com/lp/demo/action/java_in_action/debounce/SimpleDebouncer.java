package com.lp.demo.action.java_in_action.debounce;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2022/6/21 16:50
 * @desc
 **/
public class SimpleDebouncer {

    private static final ScheduledExecutorService SCHEDULE = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<Object, Future<?>> DELAYED_MAP = new ConcurrentHashMap<>();

    public static void debounce(final Object key, final Runnable runnable, long delay, TimeUnit unit) {
        final Future<?> prev = DELAYED_MAP.put(key, SCHEDULE.schedule(() -> {
            try {
                runnable.run();
            } finally {
                DELAYED_MAP.remove(key);
            }
        }, delay, unit));
        if (prev != null) {
            prev.cancel(true);
        }
    }

    public static void shutdown() {
        SCHEDULE.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleDebouncer.debounce("1", () -> {System.out.println(11);}, 3, TimeUnit.SECONDS);
        SimpleDebouncer.debounce("1", () -> {System.out.println(22);}, 3, TimeUnit.SECONDS);
        Thread.sleep(2000L);
        SimpleDebouncer.debounce("1", () -> {System.out.println(33);}, 3, TimeUnit.SECONDS);
        SimpleDebouncer.debounce("2", () -> {System.out.println(44);}, 3, TimeUnit.SECONDS);
        SimpleDebouncer.debounce("2", () -> {System.out.println(55);}, 3, TimeUnit.SECONDS);
        SimpleDebouncer.debounce("2", () -> {System.out.println(66);}, 3, TimeUnit.SECONDS);
    }
}
