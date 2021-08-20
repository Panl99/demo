package com.lp.demo.sentinel.apache.dubbo.consumer;


import com.lp.demo.sentinel.apache.dubbo.FooService;
import org.apache.dubbo.config.annotation.DubboReference;

public class FooServiceConsumer {

    @DubboReference(url = "dubbo://127.0.0.1:25758", timeout = 500)
    private FooService fooService;

    public String sayHello(String name) {
        return fooService.sayHello(name);
    }

    public String doAnother() {
        return fooService.doAnother();
    }

    public String exceptionTest(boolean biz, boolean timeout) {
        return fooService.exceptionTest(biz, timeout);
    }
}
