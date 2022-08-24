响应式编程

# Reactor简介

Reactor是基于JVM的轻量级非阻塞API，

提供了两个异步序列API：

- Mono 返回 0 或 1 个元素。
- Flux 返回 0 - n 个元素。

Mono其实可以看做Flux的子集，只包含Flux的部分功能。

常用API：

- 基本操作，filter、map、reduce、task、sort；
- 响应事件，doOnRequest/doOnNext/doOnComplete/doOnError/doOnCancel/doFinally； 
- Batch: buffer/window/groupBy；
- 多个flux操作： merge/concat/zip/combine/join；firstWithSingal/firstWithValue ； 
- 其他： Index/limitRate/log；


响应式数据流接口

- org.reactivestreams.Publisher：数据流发布者(信号从 0 到 N，N 可为无穷)。提供两个可选终端事件：错误和完成。
- org.reactivestreams.Subscriber：数据流消费者(信号从 0 到 N，N 可为无穷)。消费者初始化过程中，会请求生产者当前需要订阅多少数据。其他情况，通过接口回调与数据生产方交互: 下一条(新消息)和状态。状态包括：完成/错误，可选。
- org.reactivestreams.Subscription：初始化阶段将一个小追踪器传递给订阅者。它控制着我们准备好来消费多少数据，以及我们想要什么时候停止消费(取消)。
- org.reactivestreams.Processor：同时作为发布者和订阅者的组件的标记。


# Spring-WebFlux

[Spring WebFlux](https://docs.spring.io/spring-framework/docs/5.2.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-new-framework)

特点

1. webflux是一个异步非阻塞的Web框架,它能够充分利用多核CPU的硬件资源去处理大量的并发请
2. 内部使用的是响应式编程，以Reactor库为基础，基于异步和事件驱动，可以让我们在不扩充硬件资源的前提下，提升系统的吞吐量和伸缩性
3. 不能使接口的响应时间缩短，它仅仅能够提升吞吐量和伸缩性。

应用场景

1. 特别适合在IO密集型的服务中，比如微服务网关。
2. IO 密集型包括：磁盘IO密集型, 网络IO密集型，
    - 微服务网关就属于网络 IO 密集型，使用异步非阻塞式编程模型，能够显著地提升网关对下游服务转发的吞吐量。

Spring-WebFlux <- reactor-netty <- Reactor
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

Webflux与SpringMVC的区别

- 两个框架都可以使用注解方式，都可以运行在 Tomcat 等容器中，（Netty、Undertow、Tomcat、Jetty 和 Servlet 3.1+ 容器支持 Spring WebFlux。每个服务器都适用于一个通用的 Reactive Streams API。Spring WebFlux 编程模型建立在该通用 API 之上。默认是Netty作为服务器（NIO））
- SpringMVC 采用命令式编程，Webflux 采用异步响应式编程