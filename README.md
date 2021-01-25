# 目录
- [demo简介](#demo)
    - [demo-main](#demo-main)
    - [demo-common](#demo-common)
    - [demo-config](#demo-config)
    - [demo-register](#demo-register)
- [技术选型](#技术选型)
- [端口规划](#端口规划)
- [开发工具](#开发工具)
- [初始化项目](#初始化项目)
    - [项目部署](#项目部署)

[目录](#目录)

# demo
常用技术用法实践。

## demo-main

## demo-common

## demo-config

## demo-register

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