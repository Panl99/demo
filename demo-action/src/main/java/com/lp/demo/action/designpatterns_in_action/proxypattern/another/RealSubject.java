package com.lp.demo.action.designpatterns_in_action.proxypattern.another;

/**
 * @author lp
 * @date 2022/7/23 16:48
 * @desc 2.定义Proxy代表的真实对象
 **/
public class RealSubject extends Subject {
    @Override
    public void request() {
        System.out.println("RealSubject.request...");
    }
}
