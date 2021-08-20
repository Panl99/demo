package com.lp.demo.sentinel.apache.dubbo.consumer;

import com.alibaba.csp.sentinel.adapter.dubbo.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.lp.demo.common.util.ConsoleColorUtil;
import com.lp.demo.sentinel.apache.dubbo.FooService;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Please add the following VM arguments:
 * <pre>
 * -Djava.net.preferIPv4Stack=true
 * -Dcsp.sentinel.api.port=8721
 * -Dproject.name=dubbo-consumer-demo
 * </pre>
 */
public class FooConsumerBootstrap {

    private static final String INTERFACE_RES_KEY = FooService.class.getName();
    private static final String RES_KEY = INTERFACE_RES_KEY + ":sayHello(java.lang.String)";

    public static void main(String[] args) throws InterruptedException {
        // 初始化消费者配置
        AnnotationConfigApplicationContext consumerContext = new AnnotationConfigApplicationContext();
        consumerContext.register(ConsumerConfiguration.class);
        consumerContext.refresh();

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start interface flow<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 初始化流控规则，接口流控，不限制方法
        initFlowRule(10, false);
        FooServiceConsumer service = consumerContext.getBean(FooServiceConsumer.class);
        for (int i = 0; i < 15; i++) {
            try {
                String message = service.sayHello("interface flow");
                ConsoleColorUtil.printDefaultColor(i + " Success: " + message);
            } catch (SentinelRpcException e) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, interface flow");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(1000);

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start method flow<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 初始化流控规则，方法流控
        initFlowRule(20, true);
        for (int i = 0; i < 10; i++) {
            try {
                String message = service.sayHello("method flow");
                ConsoleColorUtil.printDefaultColor(i + " Success: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, method flow");
                ConsoleColorUtil.printDefaultColor(i + " fallback: " + service.doAnother());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Thread.sleep(1000);

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start fallback to result<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 回退到结果
        registryCustomFallback();
        for (int i = 0; i < 10; i++) {
            try {
                String message = service.sayHello("fallback to result");
                ConsoleColorUtil.printDefaultColor(i + " Result: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, fallback to result");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Thread.sleep(1000);

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start fallback to exception<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 回退到异常
        registryCustomFallbackForCustomException();
        for (int i = 0; i < 10; i++) {
            try {
                String message = service.sayHello("fallback to exception");
                ConsoleColorUtil.printDefaultColor(i + " fallback to exception, Result: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, fallback to exception");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Thread.sleep(1000);

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start fallback throw exception<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 回退抛出异常
        registryCustomFallbackWhenFallbackError();
        for (int i = 0; i < 10; i++) {
            try {
                String message = service.sayHello("fallback throw exception");
                ConsoleColorUtil.printDefaultColor(i + " fallback throw exception, Result: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, fallback throw exception");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 回退到自定义结果
     * 被流控后返回自定义结果
     */
    public static void registryCustomFallback() {
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> AsyncRpcResult.newDefaultAsyncResult("***fallback***", invocation));

    }

    /**
     * 回退到异常
     * 被流控后返回自定义异常
     */
    public static void registryCustomFallbackForCustomException() {
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> AsyncRpcResult.newDefaultAsyncResult(new RuntimeException("###fallback###"), invocation));
    }

    /**
     * 回退抛出异常
     */
    public static void registryCustomFallbackWhenFallbackError() {
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> {
            throw new RuntimeException("&&&fallback&&&");
        });
    }

    /**
     * 初始化流控规则
     *
     * @param interfaceFlowLimit 接口流控阈值
     * @param method             是否流控接口方法
     */
    private static void initFlowRule(int interfaceFlowLimit, boolean method) {
        List<FlowRule> list = new ArrayList<>();

        // 设置接口流控规则
        FlowRule flowRule = new FlowRule(INTERFACE_RES_KEY)
                .setCount(interfaceFlowLimit)
                .setGrade(RuleConstant.FLOW_GRADE_QPS);
        list.add(flowRule);

        // 设置接口方法流控规则
        if (method) {
            FlowRule flowRule1 = new FlowRule(RES_KEY)
                    .setCount(5)
                    .setGrade(RuleConstant.FLOW_GRADE_QPS);
            list.add(flowRule1);
        }

        FlowRuleManager.loadRules(list);
    }
}
