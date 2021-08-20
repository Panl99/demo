package com.lp.demo.sentinel.apache.dubbo;

public interface FooService {

    String sayHello(String name);

    String doAnother();

    String exceptionTest(boolean biz, boolean timeout);
}
