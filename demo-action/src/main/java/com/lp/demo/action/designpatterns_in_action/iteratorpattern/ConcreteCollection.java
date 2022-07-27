package com.lp.demo.action.designpatterns_in_action.iteratorpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2022/7/27 22:22
 * @desc 4.具体集合类
 **/
public class ConcreteCollection implements Collection {
    public List<Object> list = new ArrayList<>();

    @Override
    public Iterator iterator() {
        return new ConcreteIterator(this);
    }

    @Override
    public Object get(int i) {
        return list.get(i);
    }

    @Override
    public boolean add(Object o) {
        return list.add(o);
    }

    @Override
    public int size() {
        return list.size();
    }
}
