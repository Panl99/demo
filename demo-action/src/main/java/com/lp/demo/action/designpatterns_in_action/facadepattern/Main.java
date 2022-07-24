package com.lp.demo.action.designpatterns_in_action.facadepattern;

/**
 * @author lp
 * @date 2022/7/24 11:23
 * @desc 5.使用外观模式
 **/
public class Main {
    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.executePlanA();
        facade.executePlanB();
        facade.executePlanC();
    }
}
