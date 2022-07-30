package com.lp.demo.action.designpatterns_in_action.interpreterpattern;

/**
 * @author lp
 * @date 2022/7/30 22:17
 * @desc 3.定义非终结符表达式
 **/
public class NonterminalExpression implements Expression {
    // 左表达式 变量
    private Expression left;
    // 右表达式 变量
    private Expression right;

    public NonterminalExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 为语法中的非终结符实现解释操作
     * @param context
     */
    @Override
    public void interpret(Context context) {
        // 递归调用每一个组成部分的interpret();
        // 在递归调用时指定组成部分的连接方式，即非终结符的功能
        System.out.println("NonterminalExpression.interpret(context) = " + context.get(this));
        left.interpret(context);
        right.interpret(context);
    }
}
