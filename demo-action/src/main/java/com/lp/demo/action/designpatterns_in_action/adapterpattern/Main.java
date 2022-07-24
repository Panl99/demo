package com.lp.demo.action.designpatterns_in_action.adapterpattern;

import com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter.CommonSource;
import com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter.ConcreteAdapterA;
import com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter.ConcreteAdapterB;
import com.lp.demo.action.designpatterns_in_action.adapterpattern.object_adapter.ObjectAdapter;

/**
 * @author lp
 * @date 2022/7/24 22:48
 * @desc 4.使用适配器模式
 **/
public class Main {
    public static void main(String[] args) {
        // 类适配器模式
        System.out.println("---类适配器模式---");
        Target target = new Adapter();
        target.updateAll();
        target.updateName();

        // 对象适配器模式
        System.out.println("---对象适配器模式---");
        Source source = new Source();
        Target targetObj = new ObjectAdapter(source);
        targetObj.updateAll();
        targetObj.updateName();

        // 接口适配器模式
        // 在不希望实现一个接口中所有的方法时，可以创建一个抽象类AbstractAdapter实现所有方法，在使用时继承该抽象类按需实现方法。
        System.out.println("---接口适配器模式---");
        CommonSource source1 = new ConcreteAdapterA();
        CommonSource source2 = new ConcreteAdapterB();
        source1.updateName();
        source2.updateAll();
    }
}
