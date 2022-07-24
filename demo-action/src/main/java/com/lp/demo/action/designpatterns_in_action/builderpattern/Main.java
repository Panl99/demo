package com.lp.demo.action.designpatterns_in_action.builderpattern;

/**
 * @author lp
 * @date 2022/7/24 11:55
 * @desc 6.使用建造者模式
 **/
public class Main {
    public static void main(String[] args) {
        Director director = new Director();
        Builder builder1 = new ConcreteBuilder1();
        Builder builder2 = new ConcreteBuilder2();

        // 指挥者使用建造者1来构建产品
        director.construct(builder1);
        Product product1 = builder1.getResult();
        product1.show();

        // 指挥者使用建造者2来构建产品
        director.construct(builder2);
        Product product2 = builder2.getResult();
        product2.show();
    }
}
