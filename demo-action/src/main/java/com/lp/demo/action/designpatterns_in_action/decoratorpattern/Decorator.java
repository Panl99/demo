package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:35
 * @desc 3.添加抽象装饰类
 **/
public abstract class Decorator extends Component {

    protected Component component;

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        if (component != null) {
            // 运行的是Component的operation()方法
            component.operation();
        }
    }
}
