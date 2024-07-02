package com.lp.demo.spring.event.another;

import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class BEventListener {

    @Async("eventExecutor")
    @EventListener(condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).B")
    public void onApplicationEvent(BEvent event) {
        ConsoleColorUtil.printDefaultColor("receive B event = " + event + event.getTimestamp());
    }


    @Async("eventExecutor")
    @EventListener(value = {Event.class}, condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).B" +
            "|| #root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).D")
    public void onApplicationEvent2(BEvent event) {
        ConsoleColorUtil.printDefaultColor("receive B+D event = " + event + event.getTimestamp());
    }

    @Async("eventExecutor")
    @EventListener(value = {BEvent.class}, condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).D")
    public void onApplicationEvent3(BEvent event) {
        ConsoleColorUtil.printDefaultColor("receive D event = " + event + event.getTimestamp());
    }

    /**
     * 异步返回值不会作为一个新的事件被发送
     * 注意：当前写法重大错误，需要避免，
     *      当前方法接收D事件后，又返回了一个D事件，自发自收，此种操作在同步调用时会无限次发布-接收事件，最终造成StackOverflowError错误
     *
     * @param event
     * @return
     */
////    @Async("eventExecutor")
//    @EventListener(value = {BEvent.class}, condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).D")
//    public BEvent onApplicationEvent4(BEvent event) {
//        ConsoleColorUtil.printDefaultColor("receive D response event = " + event + event.getTimestamp());
//        BEvent bEvent = new BEvent(EventTypeEnum.D);
//        bEvent.setName("D -> dEvent " + bEvent.getName());
//        return bEvent;
//    }



}
