package com.lp.demo.action.designpatterns_in_action.interpreterpattern;

/**
 * @author lp
 * @date 2022/7/30 22:17
 * @desc 2.定义终结符表达式
 **/
public class TerminalExpression implements Expression {

    private String name;

    public TerminalExpression(String name) {
        this.name = name;
    }

    /**
     * 为语法中的终结符实现解释操作
     *
     * @param context
     */
    @Override
    public void interpret(Context context) {
        System.out.println("TerminalExpression.interpret(context) = " + context.get(this));
    }
}
