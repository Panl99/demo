package com.lp.demo.action.designpatterns_in_action.adapterpattern.object_adapter;

import com.lp.demo.action.designpatterns_in_action.adapterpattern.Source;
import com.lp.demo.action.designpatterns_in_action.adapterpattern.Target;

/**
 * @author lp
 * @date 2022/7/24 23:13
 * @desc 5.定义适配器类（对象适配器模式）
 *         不再继承Source类，而是持有Source类的实例，以解决兼容性问题
 **/
public class ObjectAdapter implements Target {

    private Source source;

    public ObjectAdapter(Source source) {
        super();
        this.source = source;
    }

    @Override
    public void updateName() {
        System.out.println("Adapter.updateName()...");
    }

    @Override
    public void updateAll() {
        this.source.updateAll();
        System.out.println("Adapter.updateAll()...");
    }
}
