package com.lp.demo.action.designpatterns_in_action.builderpattern;

/**
 * @author lp
 * @date 2022/7/24 21:41
 * @desc 3.具体建造者类1
 **/
public class ConcreteBuilder1 extends Builder {
    private Product product = new Product();


    @Override
    public void buildPartA() {
        product.add("ConcreteBuilder1.partA");
    }

    @Override
    public void buildPartB() {
        product.add("ConcreteBuilder1.partB");
    }

    @Override
    public Product getResult() {
        return product;
    }
}
