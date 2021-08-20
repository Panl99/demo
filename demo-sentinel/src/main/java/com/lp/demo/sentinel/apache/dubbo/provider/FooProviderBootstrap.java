package com.lp.demo.sentinel.apache.dubbo.provider;

import com.alibaba.csp.sentinel.init.InitExecutor;
import com.lp.demo.sentinel.apache.dubbo.provider.ProviderConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Provider demo for Apache Dubbo 2.7.x or above. Please add the following VM arguments:
 * <pre>
 * -Djava.net.preferIPv4Stack=true
 * -Dcsp.sentinel.api.port=8720
 * -Dproject.name=dubbo-provider-demo
 * </pre>
 *
 */
public class FooProviderBootstrap {

    public static void main(String[] args) {
        // Users don't need to manually call this method.
        // Only for eager initialization.
        InitExecutor.doInit();


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ProviderConfiguration.class);
        context.refresh();

        System.out.println("Service provider is ready");
    }

}
