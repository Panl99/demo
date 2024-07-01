package com.lp.demo.spring.event.another;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lp
 * @date 2022/10/13 15:09
 * @desc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
class EventTest {

    @Test
    void publishAEvent() {
        Event<AEvent> event = new Event<>(this, EventTypeEnum.A, new AEvent());
        EventPublisher.publish(event);
    }

    @Test
    void publishBEvent() {
        Event<BEvent> event = new Event<>(this, EventTypeEnum.B, new BEvent());
        EventPublisher.publish(event);
    }

    @Test
    void publishCEvent() {
        Event<AEvent> event = new Event<>(this, EventTypeEnum.C, new AEvent());
        EventPublisher.publish(event);
    }

    @Test
    void publishDEvent() {
        Event<BEvent> event = new Event<>(this, EventTypeEnum.D, new BEvent());
        EventPublisher.publish(event);
    }

}