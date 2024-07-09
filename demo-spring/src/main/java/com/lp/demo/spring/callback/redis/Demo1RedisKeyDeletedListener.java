//package com.lp.demo.spring.callback.redis;
//
//
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Demo1RedisKeyDeletedListener extends RedisKeyDeletedListener {
//
//    public static final String TEST = "TEST:";
//
//    public Demo1RedisKeyDeletedListener(RedisMessageListenerContainer listenerContainer) {
//        super(listenerContainer);
//    }
//
//    /**
//     * redis删除事件时回调
//     *
//     * 疑问未解决：
//     * 添加了删除事件后，当触发删除 或 key过期时，会收到不同的线程两次回调？
//     * 如果加了redisson分布式锁，会无限收到回调？？
//     *
//     * @param message redis key
//     * @param pattern del topic
//     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        String delKey = message.toString();
//        if (!delKey.startsWith(TEST)) {
//            return;
//        }
//        System.out.println("Demo1 redis key deleted event, key: " + delKey);
//    }
//
//}
