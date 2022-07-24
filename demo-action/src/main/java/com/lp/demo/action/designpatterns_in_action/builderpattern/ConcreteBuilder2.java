package com.lp.demo.action.designpatterns_in_action.builderpattern;

/**
 * @author lp
 * @date 2022/7/24 21:41
 * @desc 4.具体建造者类2
 **/
public class ConcreteBuilder2 extends Builder {
    private Product product = new Product();


    @Override
    public void buildPartA() {
        product.add("ConcreteBuilder2.partA");
    }

    @Override
    public void buildPartB() {
        product.add("ConcreteBuilder2.partB");
    }

    @Override
    public Product getResult() {
        return product;
    }
}
