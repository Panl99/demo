package com.lp.demo.action.designpatterns_in_action.proxypattern.another;

/**
 * @author lp
 * @date 2022/7/23 16:50
 * @desc 3.定义代理类，保存可以使代理访问实体的引用
 **/
public class Proxy extends Subject {
    RealSubject realSubject;

    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }

        realSubject.request();
    }
}
