//package com.lp.demo.spring.callback.redis;
//
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.core.RedisKeyspaceEvent;
//import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.Topic;
//import org.springframework.lang.Nullable;
//
//public class RedisKeyDeletedListener extends KeyspaceEventMessageListener implements ApplicationEventPublisherAware {
//
//    private static final Topic KEYEVENT_DELETED_TOPIC = new PatternTopic("__keyevent@*__:del");
//
//    @Nullable
//    private ApplicationEventPublisher publisher;
//
//    public RedisKeyDeletedListener(RedisMessageListenerContainer listenerContainer) {
//        super(listenerContainer);
//    }
//
//    @Override
//    protected void doRegister(RedisMessageListenerContainer listenerContainer) {
//        listenerContainer.addMessageListener(this, KEYEVENT_DELETED_TOPIC);
//    }
//
//    @Override
//    protected void doHandleMessage(Message message) {
//        this.publishEvent(new RedisKeyspaceEvent(KEYEVENT_DELETED_TOPIC.getTopic(), message.getBody()));
//    }
//
//    protected void publishEvent(RedisKeyspaceEvent event) {
//        if (this.publisher != null) {
//            this.publisher.publishEvent(event);
//        }
//    }
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.publisher = applicationEventPublisher;
//    }
//
//}