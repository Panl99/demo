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
        AEvent event = new AEvent(EventTypeEnum.A);
        EventPublisher.publish(event);
    }

    @Test
    void publishBEvent() {
        BEvent event = new BEvent(EventTypeEnum.B);
        EventPublisher.publish(event);
    }

    @Test
    void publishCEvent() {
        AEvent event = new AEvent(EventTypeEnum.C);
        EventPublisher.publish(event);
    }

    @Test
    void publishDEvent() {
        BEvent event = new BEvent(EventTypeEnum.D);
        EventPublisher.publish(event);
    }

}