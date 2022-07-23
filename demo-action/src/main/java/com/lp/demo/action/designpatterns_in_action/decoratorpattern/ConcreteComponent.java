package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:32
 * @desc 2.定义具体的对象类
 **/
public class ConcreteComponent extends Component {

    @Override
    public void operation() {
        System.out.println("ConcreteComponent.operation....");
    }
}
