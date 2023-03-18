

# OAuth2
Open Authentication，是一个基于令牌的安全验证和授权框架，允许用户使用第三方验证服务来进行验证。

强大之处：允许程序开发人员轻松与第三方云服务提供商集成，并使用这些服务进行用户验证和授权，而无需不断地将用户的凭据传递给第三方服务。

## OAuth2简介

### OAuth2的4个组成部分

1. 受保护资源：想要保护的资源，只有已通过验证并具有适当授权的用户才能访问。
2. 资源所有者：定义哪些用户、应用可以访问资源，`资源名称 + 密钥`是验证OAuth2令牌的部分凭证。
3. 应用程序：用户调用服务的应用程序。（用户很少直接调用服务，会依赖应用程序来调用）
4. OAuth2验证服务器：应用程序与服务之间的中间人。（允许用户直接对OAuth2服务器进行验证，不必通过应用程序传递用户凭证来调用服务）

- 用户提交凭据，通过验证来获取令牌，令牌可以在访问受保护的资源时来使用。
- 受保护的资源通过OAuth2服务器来确定令牌的有效性，并确认用户角色，角色访问资源内容。

### OAuth2四种类型授权
1. 密码（password）
2. 客户端凭据（client credential）
3. 授权码（authorization code）
4. 隐式（implicit）

### 验证与授权
- 验证`authentication`：用户通过提供凭据来证明他们是谁的行为。
- 授权`authorization`：是否允许用户做他们想做的事情。


1. 验证用户

**请求：**

Spring OAuth2服务接口
- POST `{url}/auth/oauth/token`

Authorization
- `Type`：`Basic Auth`
- `Username`：eagleeye（应用名称）
- `Password`：thisissecret（应用程序密钥）

HTTP表单参数：（下列参数不作为body传递）
- `grant_type`：OAuth2授权类型。本例中使用密码（password）授权
- `scope`：应用作用域。注册应用时定义的（webclient、mobileclient）
- `user_name`：用户登录名。（zhangsan）
- `password`：用户登录密码。（123456）

**返回：**

- `access_token`：OAuth2令牌。（"xxx-xxx-xxx"）
- `token_type`：令牌类型。此处使用常用的不记名令牌`bearer token`。（bearer）
- `refresh_token`：刷新令牌，OAuth2令牌过期时，可使用此令牌向OAuth2服务器请求重新颁发令牌。（"xxx-xxx-xxx"）
- `expires_in`：OAuth2令牌过期前的秒数。Spring中默认12小时。（42040）
- `scope`：此OAuth2令牌的有效作用域。（webclient）


2. 访问资源

**请求：**

用户接口
- GET `{url}/auth/user`

Headers
- Authorization: Bearer xxx-xxx-xxx

**返回：**

```json
{
  "user": {
    "password": null,
    "username": "zhangsan",
    "authorities": [
      {
        "authority": "ROLE_USER" // Spring将前缀 ROLE_ 分配给用户角色，这里表示zhangsan拥有USER角色。
      }    
    ],
    "": "",
    ...
  },
  "authorities": [
    "ROLE_USER"   
  ]
}
```

## Spring Cloud Security + OAuth2

要创建受保护的资源，需要以下操作：
- 将相应的Spring Security 和 OAuth2的jar添加到要保护的服务中。
- 配置服务指向OAuth2验证服务。
- 定义谁可以访问服务。


1. **各服务添加Spring Security 和 OAuth2依赖**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-security</artifactId>
    <version>2.2.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security.oauth</groupId>
    <artifactId>spring-security-oauth2</artifactId>
    <version>2.3.4.RELEASE</version>
</dependency>
```

2. **配置受保护服务指向OAuth2验证服务**
```yaml
security:
  oauth2:
    resource:
      userInfoUri: http://localhost:8901/auth/user
```

并告知受保护的服务它是受保护的资源：在受保护的服务的引导类Application.java 上添加注解：`@EnableResourceServer`。
    - `@EnableResourceServer`会强制执行一个过滤器，拦截对该服务的全部调用，检查HTTP请求头中是否包含OAuth2访问令牌，
    - 然后调用`security.oauth2.resource.userInfoUri`定义的回调url来检查令牌是否有效。
    - 如果有效，该注解会应用任何访问控制规则，以控制什么人可以访问服务。


3. **定义谁可以访问服务**

服务定义访问控制规则，需要扩展`ResourceServerConfigurerAdapter`类，覆盖`configure()`方法。

[ResourceServerConfiguration.java](src/main/java/com/lp/demo/auth/oauth2/organization/security/ResourceServerConfiguration.java)

- 1.配置允许通过验证的**用户**才能访问资源：
```java
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 限制对受保护服务中所有url的访问，仅限通过身份认证的用户访问。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}
```

- 2.配置允许通过验证的**角色**才能访问资源：
```java
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 限制对受保护服务中/v1/organizations/开头的url的DELETE方法访问，仅限通过身份认证的ADMIN角色用户访问。
     * 其他不在上述匹配的url由已通过验证的用户来访问
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/v1/organizations/**") // 对/v1/organizations/下的所有DELETE方法限制访问
                .hasRole("ADMIN")   // 具有ADMIN角色的用户才能访问
                .anyRequest()   //　其他不在匹配的url由已通过验证的用户来访问
                .authenticated();
    }sentinel
}
```

4. **传播OAuth2访问令牌**

在服务之间传播OAuth2令牌

- 用户向OAuth2服务器进行认证，并获取到OAuth2访问令牌。
- 