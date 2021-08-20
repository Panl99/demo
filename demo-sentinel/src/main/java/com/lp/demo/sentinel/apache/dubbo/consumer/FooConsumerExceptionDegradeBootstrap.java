package com.lp.demo.sentinel.apache.dubbo.consumer;

import com.alibaba.csp.sentinel.adapter.dubbo.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.lp.demo.common.util.ConsoleColorUtil;
import com.lp.demo.sentinel.apache.dubbo.FooService;
import com.lp.demo.sentinel.apache.dubbo.consumer.ConsumerConfiguration;
import com.lp.demo.sentinel.apache.dubbo.consumer.FooServiceConsumer;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Please add the following VM arguments:
 * <pre>
 * -Djava.net.preferIPv4Stack=true
 * -Dcsp.sentinel.api.port=8721
 * -Dproject.name=dubbo-consumer-demo
 * </pre>
 * 消费异常降级
 */
public class FooConsumerExceptionDegradeBootstrap {

    private static final String INTERFACE_RES_KEY = FooService.class.getName();
    private static final String RES_KEY = INTERFACE_RES_KEY + ":sayHello(java.lang.String)";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 初始化消费者配置
        AnnotationConfigApplicationContext consumerContext = new AnnotationConfigApplicationContext();
        consumerContext.register(ConsumerConfiguration.class);
        consumerContext.refresh();

        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>start ConsumerExceptionDegrade<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        FooServiceConsumer service = consumerContext.getBean(FooServiceConsumer.class);
        // 设置降级规则
        initExceptionFallback(3);
        // 设置自定义回退值
        registryCustomFallback();
        for (int i = 0; i < 10; i++) {
            try {
                // biz=true直接抛异常
                String message = service.exceptionTest(true, false);
                ConsoleColorUtil.printDefaultColor(i + " exceptionTest throw exception, Result: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, exceptionTest throw exception");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        initExceptionFallback(3);
        // 睡眠3s跳过时间窗口
        Thread.sleep(3000);
        for (int i = 0; i < 10; i++) {
            try {
                String message = service.exceptionTest(false, true);
                ConsoleColorUtil.printDefaultColor(i + " exceptionTest timeout, Result: " + message);
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, exceptionTest timeout");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        initExceptionFallback(3);
        Thread.sleep(3000);
        try {
            // 超时触发回退
            CompletableFuture<String> completableFuture = RpcContext.getContext().asyncCall(() -> service.exceptionTest(false, true));
            ConsoleColorUtil.printDefaultColor("asyncCall timeout, Result: " + completableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i++) {
            try {
                CompletableFuture<String> result = RpcContext.getContext().asyncCall(() -> service.exceptionTest(false, true));
                ConsoleColorUtil.printDefaultColor(i + " asyncCall timeout, Result: " + result.get());
            } catch (SentinelRpcException ex) {
                ConsoleColorUtil.printDefaultColor(i + " Blocked, asyncCall timeout");
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
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> AsyncRpcResult.newDefaultAsyncResult("fallback", invocation));
    }

    /**
     * 设置降级规则
     * @param timewindow
     */
    public static void initExceptionFallback(int timewindow) {
        DegradeRule degradeRule = new DegradeRule(INTERFACE_RES_KEY)
                .setCount(0.5)
                .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO)
                .setTimeWindow(timewindow)
                .setMinRequestAmount(1);
        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
    }
}
