# 关键组件说明

- `@EnableFeignClients`：启用 Feign客户端扫描。
- `@FeignClient`：定义 Feign 客户端的入口，用于声明一个可调用的远程服务。
    - `name/value`：指定服务名称（对应注册中心的服务 ID）。
    - `url`：直接指定服务的基础 URL（适用于非注册中心场景）。
    - `path`：公共路径前缀（例如 "/api/v1"）。
    - `configuration`：自定义配置类（用于覆盖默认配置）。
    - `fallback/fallbackFactory`：定义服务降级处理类。
- `FeignContext`：存储 Feign客户端的相关配置。
- `Targeter`：负责创建 Feign客户端实例。
- `Contract`：解析接口上的注解，生成元数据。
- `Encoder/Decoder`：处理请求体和响应体的序列化和反序列化。
- `Interceptor`：拦截 HTTP 请求和响应，进行额外的处理。
- `Client`：实际执行 HTTP 请求的客户端，如 Apache HttpClient 或 OkHttp。



