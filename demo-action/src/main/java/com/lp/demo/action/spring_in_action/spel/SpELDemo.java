package com.lp.demo.action.spring_in_action.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


public class SpELDemo {

    public static void main(String[] args) {
        Boolean s = test(20);
        System.out.println("s = " + s); // s = true

        test1();
        testParserContext();
        test2();
        test3();
        test4();
        testClassTypeExpression();
    }

    /**
     * 验证数字是否大于10 * * @param number 数字 * @return 结果
     */
    public static Boolean test(int number) {
        // 创建ExpressionParser对象，用于解析SpEL表达式
        ExpressionParser parser = new SpelExpressionParser();
        String expressionStr = "#number > 10 ? true : 'false'";
        Expression expression = parser.parseExpression(expressionStr);
        // 创建EvaluationContext对象，用于设置参数值
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("number", number);
        // 求解表达式，获取结果
        return expression.getValue(context, Boolean.class);
    }

    public static void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println("test1 " + expression.getValue(context));
    }

    /**
     * 使用ParserContext的情况，此处定义了ParserContext实现：定义表达式是模块，表达式前缀为“#{”，后缀为“}”；使用parseExpression解析时传入的模板必须以“#{”开头，以“}”结尾，如"#{'Hello '}#{'World!'}"。
     */
    public static void testParserContext() {
        ExpressionParser parser = new SpelExpressionParser();
        ParserContext parserContext = new ParserContext() {
            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return "#{";
            }

            @Override
            public String getExpressionSuffix() {
                return "}";
            }
        };
        String template = "#{'Hello '}#{'World!'}";
        Expression expression = parser.parseExpression(template, parserContext);
        System.out.println("testParserContext " + expression.getValue());
    }


    /**
     * 字面量表达式
     */
    public static void test2() {
        ExpressionParser parser = new SpelExpressionParser();

        String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);
        int int1 = parser.parseExpression("1").getValue(Integer.class);
        long long1 = parser.parseExpression("-1L").getValue(long.class);
        float float1 = parser.parseExpression("1.1").getValue(Float.class);
        double double1 = parser.parseExpression("1.1E+2").getValue(double.class);
        int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
        long hex2 = parser.parseExpression("0xaL").getValue(long.class);
        boolean true1 = parser.parseExpression("true").getValue(boolean.class);
        boolean false1 = parser.parseExpression("false").getValue(boolean.class);
        Object null1 = parser.parseExpression("null").getValue(Object.class);

        System.out.println("test2 str1=" + str1);
        System.out.println("test2 int1=" + int1);
        System.out.println("test2 long1=" + long1);
        System.out.println("test2 float1=" + float1);
        System.out.println("test2 double1=" + double1);
        System.out.println("test2 hex1=" + hex1);
        System.out.println("test2 hex2=" + hex2);
        System.out.println("test2 true1=" + true1);
        System.out.println("test2 false1=" + false1);
        System.out.println("test2 null1=" + null1);
        /**
         * str1=Hello World!
         * int1=1
         * long1=-1
         * float1=1.1
         * double1=110.0
         * hex1=10
         * hex2=10
         * true1=true
         * false1=false
         * null1=null
         */
    }

    /**
     * 算数运算表达式：SpEL支持加(+)、减(-)、乘(*)、除(/)、求余（%）、幂（^）运算。还提供求余（MOD）和除（DIV）而外两个运算符，与“%”和“/”等价，不区分大小写。
     * 关系表达式：等于（==）、不等于(!=)、大于(>)、大于等于(>=)、小于(<)、小于等于(<=)，区间（between）运算。
     */
    public static void test3() {
        ExpressionParser parser = new SpelExpressionParser();
        boolean v1 = parser.parseExpression("1>2").getValue(boolean.class);
        boolean between1 = parser.parseExpression("1 between {1,2}").getValue(boolean.class);
        System.out.println("test3 v1=" + v1);
        System.out.println("test3 between1=" + between1);
    }

    /**
     * 逻辑表达式：且（and或者&&）、或(or或者||)、非(!或NOT)。
     */
    public static void test4() {
        ExpressionParser parser = new SpelExpressionParser();

        boolean result1 = parser.parseExpression("2>1 and (!true or !false)").getValue(boolean.class);
        boolean result2 = parser.parseExpression("2>1 && (!true || !false)").getValue(boolean.class);

        boolean result3 = parser.parseExpression("2>1 and (NOT true or NOT false)").getValue(boolean.class);
        boolean result4 = parser.parseExpression("2>1 && (NOT true || NOT false)").getValue(boolean.class);

        System.out.println("test4 result1=" + result1);
        System.out.println("test4 result2=" + result2);
        System.out.println("test4 result3=" + result3);
        System.out.println("test4 result4=" + result4);
    }

    /**
     * 字符串连接及截取表达式：使用“+”进行字符串连接，使用“'String'[0] [index]”来截取一个字符，目前只支持截取一个，如“'Hello ' + 'World!'”得到“Hello World!”；而“'Hello World!'[0]”将返回“H”。
     * 三目运算符： “表达式1?表达式2:表达式3”用于构造三目运算表达式，如“2>1?true:false”将返回true；
     * 正则表达式：使用“str matches regex，如“'123' matches '\d{3}'”将返回true；
     * 括号优先级表达式：使用“(表达式)”构造，括号里的具有高优先级。
     */


    /**
     * 类类型表达式
     */
    public static void testClassTypeExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        //java.lang包类访问
        Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);
        System.out.println("testClassTypeExpression " + result1);

        //其他包类访问
        String expression2 = "T(com.lp.demo.action.spring_in_action.spel.SpELDemo)";
        Class<SpELDemo> value = parser.parseExpression(expression2).getValue(Class.class);
        System.out.println("testClassTypeExpression " + (value == SpELDemo.class));

        //类静态字段访问
        int result3 = parser.parseExpression("T(Integer).MAX_VALUE").getValue(int.class);
        System.out.println("testClassTypeExpression " + (result3 == Integer.MAX_VALUE));

        //类静态方法调用
        int result4 = parser.parseExpression("T(Integer).parseInt('1')").getValue(int.class);
        System.out.println("testClassTypeExpression " + result4);
    }

    /**
     * 更多参考：https://www.jianshu.com/p/a8b2d5886129
     */

}
