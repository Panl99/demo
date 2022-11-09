package com.lp.demo.spring.event;


import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2022/10/12 15:48
 * @desc 事件监听器
 **/

/**
 * 1.作为类时，直接监听对应事件类型，只执行对应的的事件（UserInfoEvent、UserRegisterEvent）。
 */
@Component
public class EventServiceListener<T> implements ApplicationListener<Event<T>> {

//    @Override
//    void onApplicationEvent(Event<T> event);

    @Override
    public void onApplicationEvent(Event<T> event) {
        ConsoleColorUtil.printDefaultColor(">>>>>>>EventServiceListener.onApplicationEvent()");
        event.execute(event.getData());
    }


//    @Async
//    @Override
//    public void onApplicationEvent(Event<T> event) {
//        ConsoleColorUtil.printDefaultColor(">>>>>>>onApplicationEvent");
//        event.execute(event.getData());
//    }
//
//    @EventListener
//    public void onApplicationEvent(UserInfoEvent userInfoEvent) {
//        ConsoleColorUtil.printDefaultColor(">>>>>>>UserInfoEventListener");
//        userInfoEvent.execute(userInfoEvent.getData());
//    }

}

/**
 * 2.作为接口时，由具体对应监听处理器来实现（UserInfoEventListener、UserRegisterEventListener），
 * 则不论发送的是什么类型的事件（UserInfoEvent、UserRegisterEvent），两个监听器👆都会收到事件并进行处理。
 */
//@Component
//public interface EventServiceListener<T> extends ApplicationListener<Event<T>> {
//    @Override
//    default void onApplicationEvent(Event<T> event) {
//        ConsoleColorUtil.printDefaultColor(">>>>>>>EventServiceListener.onApplicationEvent");
//        event.execute(event.getData());
//    }
//}
