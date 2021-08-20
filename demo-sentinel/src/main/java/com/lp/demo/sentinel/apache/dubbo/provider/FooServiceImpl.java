package com.lp.demo.sentinel.apache.dubbo.provider;


import com.lp.demo.sentinel.apache.dubbo.FooService;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDateTime;

@DubboService
public class FooServiceImpl implements FooService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s at %s", name, LocalDateTime.now());
    }

    @Override
    public String doAnother() {
        return LocalDateTime.now().toString();
    }

    @Override
    public String exceptionTest(boolean biz, boolean timeout) {
        if (biz) {
            throw new RuntimeException("biz exception");
        }
        if (timeout) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "Success";
    }

}
