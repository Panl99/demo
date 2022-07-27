package com.lp.demo.action.designpatterns_in_action.iteratorpattern;

/**
 * @author lp
 * @date 2022/7/27 22:15
 * @desc 3.定义具体迭代器类
 **/
public class ConcreteIterator implements Iterator {

    private final Collection collection;

    // 当前迭代器遍历到的元素位置
    private int pos = -1;

    public ConcreteIterator(Collection collection) {
        this.collection = collection;
    }

    @Override
    public Object previous() {
        if (pos > 0) {
            pos--;
        }
        return collection.get(pos);
    }

    @Override
    public Object next() {
        if (pos < collection.size() - 1) {
            pos++;
        }
        return collection.get(pos);
    }

    @Override
    public boolean hasNext() {
        return pos < collection.size() - 1;
    }

}
