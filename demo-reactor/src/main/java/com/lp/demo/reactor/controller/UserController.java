package com.lp.demo.reactor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@Slf4j
@RestController
public class UserController {

    /**
     * 首先看index1和index2，其实这两个的返回效果在前端都很一致，都是三秒后前端展示对应的结果，
     * 区别在于，前者要等待执行结束后才会继续往下走，而后者直接返回相关结果（即实现了异步返回）
     *  index3 , 主要的区别在于返回了多个结果
     *  而对于index4，区别则在于 produces = MediaType.TEXT_EVENT_STREAM_VALUE，其实内容与index3 类似，只是返回的结果是一个一个的返回。
     *  对于index5，这里展示不是很好展示，执行结束后的结果如index4 类似，只是结果不是同一个时间返回的，而是每一个item之间间隔了1秒，像动画一样一个一个出现的。
     *
     *  WebFlux要点
     *      可以使用SpringMvc的方式进行开发
     *      构建WebFlux环境启动时，应用服务器默认是Netty
     *      从调用方来看，没有变化，但是服务端是异步返回的
     *      WebFlux对象还支持服务器推送
     */

    @RequestMapping("/index1")
    public String index() {
        log.info("mvc index start。。。");
        String result = getData();
        log.info("mvc index end。。。。");
        return result;
    }

    @RequestMapping("/index2")
    public Mono<String> index2() {
        log.info("flux index start。。。");
        Mono<String> result = Mono.fromSupplier(()->getData());
        log.info("flux index end。。。。");
        return result;
    }

    @RequestMapping(value = "/index3")
    public Flux<String> index3() {
        log.info("flux index start。。。");
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i -> {
            return "flux item " + i;
        }));
        log.info("flux index end。。。。");
        return result;
    }

    @RequestMapping(value = "/index4", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // 像流一样，一个一个的返回
    public Flux<String> index4() {
        log.info("flux index start。。。");
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj( i -> {
            return "flux item " + i;
        }));
        log.info("flux index end。。。。");
        return result;
    }

    @RequestMapping(value = "/index5", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> index5() {
        log.info("flux index start。。。");
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj( i -> {
            try {
                Thread.sleep(1000);// 模拟休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "flux item " + i;
        }));
        log.info("flux index end。。。。");
        return result;
    }


    // 获取数据
    public String getData() {
        try {
            // 模拟延迟获取数据
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world from get data method!";
    }

}