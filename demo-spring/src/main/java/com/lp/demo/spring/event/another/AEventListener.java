package com.lp.demo.spring.event.another;

import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class AEventListener {


    @EventListener(value = {AEvent.class}, condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).A")
    public void onApplicationEvent(AEvent event) {
        ConsoleColorUtil.printDefaultColor("receive A void event = " + event + event.getTimestamp());
    }

    /**
     * 返回值会作为一个新的事件被发送（仅同步调用，异步不会）
     *
     * @param event 接收事件A
     * @return
     */
    @EventListener(condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).A")
    public BEvent onApplicationEvent4Resp(AEvent event) {
        ConsoleColorUtil.printDefaultColor("receive A response BEvent event = " + event + event.getTimestamp());
        BEvent bEvent = new BEvent(EventTypeEnum.B);
        bEvent.setName("A -> bEvent " + bEvent.getName());
        return bEvent;
    }

    /**
     * 返回值是列表或数组，每个子项都会作为一个新的事件被发送
     *
     * @param event 接收事件A、C
     * @return
     */
    @EventListener(condition = "#root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).A " +
            "|| #root.event.source == T(com.lp.demo.spring.event.another.EventTypeEnum).C")
    public List<BEvent> onApplicationEvent4RespArray(AEvent event) {
        ConsoleColorUtil.printDefaultColor("receive A response List<BEvent> event = " + event + event.getTimestamp());
        BEvent bEvent = new BEvent(EventTypeEnum.B);
        bEvent.setName("A -> bEvent1 " + bEvent.getName());

        BEvent bEvent2 = new BEvent(EventTypeEnum.D);
        bEvent2.setName("A -> bEvent2 " + bEvent2.getName());

        return Arrays.asList(bEvent, bEvent2);
    }

}
