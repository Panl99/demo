package com.lp.demo.spi;

import com.lp.demo.spi.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.ServiceLoader;

@SpringBootTest
class DemoSpiApplicationTests {

    @Test
    void contextLoads() {
    }

    // 注意maven不可以pom形式打包，target下不会打包META-INF下文件
    @Test
    void spiTest() {
        ServiceLoader<HelloService> loader = ServiceLoader.load(HelloService.class);
        Iterator<HelloService> iterator = loader.iterator();
        while (iterator.hasNext()) {
            HelloService next = iterator.next();
            next.hello();
        }
    }

}
