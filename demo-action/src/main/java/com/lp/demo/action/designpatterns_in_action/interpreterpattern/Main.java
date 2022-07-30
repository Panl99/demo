package com.lp.demo.action.designpatterns_in_action.interpreterpattern;


/**
 * @author lp
 * @date 2022/7/30 21:29
 * @desc 5.使用解释器模式
 * TODO
 **/
public class Main {
    public static void main(String[] args) {
        Context context = new Context();

        TerminalExpression t1 = new TerminalExpression("T1");
        TerminalExpression t2 = new TerminalExpression("T2");
        TerminalExpression t3 = new TerminalExpression("T3");
        TerminalExpression t4 = new TerminalExpression("T4");
        TerminalExpression t5 = new TerminalExpression("T5");

        context.set(t1, "v1");
        context.set(t2, "v2");
        context.set(t3, "v3");

        NonterminalExpression nonterminalExpression = new NonterminalExpression(t4, t5);
        nonterminalExpression.interpret(context);
    }
}
