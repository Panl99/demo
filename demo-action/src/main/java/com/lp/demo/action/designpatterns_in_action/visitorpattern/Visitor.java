package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:07
 * @desc 1.定义访问者接口
 **/
public interface Visitor {

    void visit(ConcreteElementA elementA);
}
