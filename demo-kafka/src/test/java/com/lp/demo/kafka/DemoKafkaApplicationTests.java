package com.lp.demo.kafka;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@EmbeddedKafka(count = 4, ports = {9092, 9093, 9094, 9095})
@SpringBootTest
class DemoKafkaApplicationTests {

    @Test
    void contextLoads() throws IOException {
        System.in.read();
    }

}
