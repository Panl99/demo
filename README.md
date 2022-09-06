# 目录
- [demo简介](#demo)
  - [demo-action](#demo-action)
  - [demo-main](#demo-main)
  - [demo-common](#demo-common)
  - [demo-redis](#demo-redis)
  - [demo-netty](#demo-netty)
  - [demo-websocket](#demo-websocket)
  - [demo-eureka](#demo-eureka)
  - [demo-kafka](#demo-kafka)
  - [demo-rabbitmq](#demo-rabbitmq)
  - [demo-brcc](#demo-brcc)
  - [demo-dubbo](#demo-dubbo)
  - [demo-nacos](#demo-nacos)
  - [demo-iot](#demo-iot)
  - [demo-seata](#demo-seata)
  - [demo-sentinel](#demo-sentinel)
  - [demo-auth](#demo-auth)
  - [demo-xxljob](#demo-xxljob)  
  - [demo-api](#demo-api)
  - [demo-action](#demo-action)
  - [demo-spi](#demo-spi)
  - [demo-reactor](#demo-reactor)
  - [demo-spring](#demo-spring)
- [技术选型](#技术选型)
- [端口规划](#端口规划)
- [开发工具](#开发工具)
- [初始化项目](#初始化项目)
    - [项目部署](#项目部署)

[目录](#目录)

# demo
常用技术用法实践。

## demo-action
[README.md](demo-action/README.md)
- [Java实战 (java_in_action)](demo-action/src/main/java/com/lp/demo/action/java_in_action)
- [设计模式 (designpatterns_in_action)](demo-action/src/main/java/com/lp/demo/action/designpatterns_in_action)
- [算法题 (leetcode_in_action)](demo-action/src/main/java/com/lp/demo/action/leetcode_in_action)
- [Spring系列实战 (spring_in_action)](demo-action/src/main/java/com/lp/demo/action/spring_in_action)

## demo-main
- [MainController](demo-main/src/main/java/com/lp/demo/main/controller/MainController.java)
  - 简单rest接口测试
- [UserController](demo-main/src/main/java/com/lp/demo/main/controller/UserController.java)
  - [aop测试](demo-common/src/main/java/com/lp/demo/common/aop)

## demo-common
- [工具类demo](demo-common/src/main/java/com/lp/demo/common/util)
  - [hutool](demo-common/src/main/java/com/lp/demo/common/util/hutool)：hutool工具类测试
    - [ExcelUtilDemo](demo-common/src/main/java/com/lp/demo/common/util/hutool/ExcelUtilDemo.java)：Excel工具使用示例
    - [JsonUtilDemo](demo-common/src/main/java/com/lp/demo/common/util/hutool/JsonUtilDemo.java)：Json工具使用示例
    - [StrUtilDemo null](demo-common/src/main/java/com/lp/demo/common/util/hutool/StrUtilDemo.java)：字符串工具使用示例
  - [BloomFilterUtil](demo-common/src/main/java/com/lp/demo/common/util/BloomFilterUtil.java)：布隆过滤器示例
  - [ConcurrentHashSet](demo-common/src/main/java/com/lp/demo/common/util/ConcurrentHashSet.java)：ConcurrentHashSet实现
  - [ConsoleColorUtil](demo-common/src/main/java/com/lp/demo/common/util/ConsoleColorUtil.java)：System.out.println打印自定义文本颜色、状态
  - [DateUtil](demo-common/src/main/java/com/lp/demo/common/util/DateUtil.java)：日期时间工具示例
    - ChronoUnit：时间单位枚举
  - [JsonUtil](demo-common/src/main/java/com/lp/demo/common/util/JsonUtil.java)：Json工具示例
  - [~~ObjectUtil~~](demo-common/src/main/java/com/lp/demo/common/util/ObjectUtil.java)~~：对象工具类（对象校验、Bean转换）~~ 可使用mapstruct进行bean转换
  - [mapstruct](demo-common/src/main/java/com/lp/demo/common/util/mapstruct)：mapstruct对象转换工具（编译期运行生成getter/setter，比spring的BeanUtils使用反射 效率高）[官方文档](https://mapstruct.org/documentation/stable/reference/html/)
  - [RandomUtil](demo-common/src/main/java/com/lp/demo/common/util/RandomUtil.java)：随机数工具（安全随机数）
  - [StringUtil](demo-common/src/main/java/com/lp/demo/common/util/StringUtil.java)：字符串工具（字符串校验）
  - [SystemStatusInfoStatisticsUtil](demo-common/src/main/java/com/lp/demo/common/util/SystemStatusInfoStatisticsUtil.java)：系统状态统计（cpu、堆栈、内存等使用率）
- [文件解析demo TODO](demo-common/src/main/java/com/lp/demo/common/parsefile)

- [aop](demo-common/src/main/java/com/lp/demo/common/aop)：aop定义
  - [ThreadPoolUtil](demo-common/src/main/java/com/lp/demo/common/aop/thread/ThreadPoolUtil.java)：线程池工具
- [async](demo-common/src/main/java/com/lp/demo/common/async)：@Async异步任务定义、测试
- [enums](demo-common/src/main/java/com/lp/demo/common/enums)：枚举目录
  - [ZoneIdEnum](demo-common/src/main/java/com/lp/demo/common/enums/ZoneIdEnum.java)：时区枚举定义
- [exception](demo-common/src/main/java/com/lp/demo/common/exception)：异常目录
  - [DisplayableException](demo-common/src/main/java/com/lp/demo/common/exception/DisplayableException.java)：自定义抛异常
  - [GlobalExceptionHandler](demo-common/src/main/java/com/lp/demo/common/exception/GlobalExceptionHandler.java)：全局异常处理
- [result](demo-common/src/main/java/com/lp/demo/common/result)：result目录
  - [JsonResult](demo-common/src/main/java/com/lp/demo/common/result/JsonResult.java)：返回结果定义
  - [ResultEnum](demo-common/src/main/java/com/lp/demo/common/result/ResultEnum.java)：返回结果枚举值定义
- [service](demo-common/src/main/java/com/lp/demo/common/service)：service目录
  - [ThreadMetricService](demo-common/src/main/java/com/lp/demo/common/service/ThreadMetricService.java)：线程统计服务
  - [ThreadLocalService](demo-common/src/main/java/com/lp/demo/common/service/ThreadLocalService.java)：ThreadLocal服务
  - [ThreadPoolService](demo-common/src/main/java/com/lp/demo/common/service/ThreadPoolService.java)：自定义线程池服务
- [test](demo-common/src/main/java/com/lp/demo/common/test)：测试目录
  - [ThreadLocalTest](demo-common/src/main/java/com/lp/demo/common/test/ThreadLocalTest.java)：ThreadLocal简单测试

## demo-redis

## demo-netty

## demo-websocket

## demo-eureka

## demo-kafka

## demo-rabbitmq


## demo-brcc
百度开源配置中心。

- [brcc.github](https://github.com/baidu/brcc)
- [brcc.md](https://github.com/Panl99/codebook/blob/master/configcenter/brcc.md)

## demo-dubbo
阿里-Apache开源RPC框架

## demo-nacos
阿里开源注册中心、配置中心

## demo-iot
iot相关

- [设备事件服务](demo-iot/src/main/java/com/lp/demo/iot/service/DeviceEventService.java)
  - 策略模式，按业务类型、事件类型分发给具体Handler处理。

## demo-seata
阿里开源的分布式事务组件。

- [官方文档](https://seata.io/zh-cn/docs/overview/what-is-seata.html)


## demo-sentinel
阿里开源的分布式流控组件

- [github wiki](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D)

## demo-auth

### [jwt](../demo/demo-auth/src/main/java/com/lp/demo/auth/jwt)


## demo-xxljob

## demo-api

## demo-action

## demo-spi

## demo-reactor

## demo-spring


[目录](#目录)

# 技术选型
|框架|作用|链接|
|---|---|---|
|Spring Boot           |起步依赖，自动配置，简化Spring开发|[SpringBoot-2.4.1-docs](https://docs.spring.io/spring-boot/docs/2.4.1/reference/html/using-spring-boot.html#using-boot-maven)|
|[Spring Cloud组件选型](#SpringCloud组件选型)|各种组件集成|[SpringCloud-2020.0.0-docs](https://docs.spring.io/spring-cloud/docs/2020.0.0/reference/html/)|
|SpringSecurity        | 认证和授权框架                |[https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)|
| MyBatis              | ORM框架                       | [http://www.mybatis.org/mybatis-3/zh/index.html](http://www.mybatis.org/mybatis-3/zh/index.html)      
| MyBatisGenerator     | 数据层代码生成                | [http://www.mybatis.org/generator/index.html](http://www.mybatis.org/generator/index.html)         
| PageHelper           | MyBatis物理分页插件           | [http://git.oschina.net/free/Mybatis_PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)      
| Swagger-UI           | 文档生产工具                  | [https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui)           
| Hibernator-Validator | 验证框架                      | [http://hibernate.org/validator](http://hibernate.org/validator)                      
| Elasticsearch        | 搜索引擎                      | [https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch)            
| RabbitMq             | 消息队列                      | [https://www.rabbitmq.com/](https://www.rabbitmq.com/)                           
| Redis                | 分布式缓存                    | [https://redis.io/](https://redis.io/)                                   
| MongoDb              | NoSql数据库                   | [https://www.mongodb.com](https://www.mongodb.com)                             
| Docker               | 应用容器引擎                  | [https://www.docker.com](https://www.docker.com)                              
| Druid                | 数据库连接池                  | [https://github.com/alibaba/druid](https://github.com/alibaba/druid)                    
| OSS                  | 对象存储                      | [https://github.com/aliyun/aliyun-oss-java-sdk](https://github.com/aliyun/aliyun-oss-java-sdk)       
| MinIO                | 对象存储                      | [https://github.com/minio/minio](https://github.com/minio/minio)                      
| JWT                  | JWT登录支持                   | [https://github.com/jwtk/jjwt](https://github.com/jwtk/jjwt)                        
| LogStash             | 日志收集工具                  | [https://github.com/logstash/logstash-logback-encoder](https://github.com/logstash/logstash-logback-encoder)
| Lombok               | 简化对象封装工具              | [https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok)   
|Jenkins               |自动化部署工具                  |[https://github.com/jenkinsci/jenkins](https://github.com/jenkinsci/jenkins)|

## SpringCloud组件选型
|组件|作用|链接|
|---|---|---|
|Consul|服务发现与注册中心(其他：SpringCloudZookeeper、Nacos)||
|Feign|端到端调用||
|Hystrix|断路器||
|Turbine|Hystrix监控聚合||
|Hystrix Dashboard|Hystrix监控界面||
|SpringCloudGateway|网关||

[目录](#目录)

# 端口规划
项目|端口
---|---
config-server|18888
Consul|8500
hystrix-dashboard|7979
turbine-server|8989
Gateway|


[目录](#目录)

# 开发工具
|工具|说明|链接|
|---|---|---|
|IDEA|开发IDE|[idea-download](https://www.jetbrains.com/idea/download)|
|Axure|原型设计工具|[https://www.axure.com](https://www.axure.com)|
|ScreenToGif|gif录制工具|[https://www.screentogif.com](https://www.screentogif.com)、[gif动图制作网站](https://giphy.com)|

[目录](#目录)

# 初始化项目

## 项目部署

[目录](#目录)