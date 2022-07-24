package com.lp.demo.action.designpatterns_in_action.facadepattern;

/**
 * @author lp
 * @date 2022/7/24 11:45
 * @desc 4.定义外观类
 *         外观类需要了解所有子系统的方法和属性，进行组合，以备外部调用
 **/
public class Facade {
    private SubSystem1 system1;
    private SubSystem2 system2;
    private SubSystem3 system3;

    public Facade() {
        this.system1 = new SubSystem1();
        this.system2 = new SubSystem2();
        this.system3 = new SubSystem3();
    }

    public void executePlanA() {
        System.out.println("Facade.executePlanA...");
        system1.execute();
        system2.execute();
        system3.execute();
    }

    public void executePlanB() {
        System.out.println("Facade.executePlanB...");
        system2.execute();
        system3.execute();
    }

    public void executePlanC() {
        System.out.println("Facade.executePlanC...");
        system1.execute();
        system3.execute();
    }

}
