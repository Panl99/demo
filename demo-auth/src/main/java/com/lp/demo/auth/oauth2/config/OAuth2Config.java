package com.lp.demo.auth.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * @author lp
 * @date 2022/10/24 21:42
 * @desc 配置OAuth2服务知道的应用程序 和用户凭据。
 **/
@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 定义通过验证服务注册了哪些客户端应用程序
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 定义哪些客户端将注册到服务
        clients.inMemory() // ClientDetailsServiceConfigurer支持两种存储，内存存储、jdbc存储，这里使用内存
                .withClient("eagleeye") // 注册的应用程序的名称
                .secret("thisissecret") // 注册的应用密钥，在调用OAuth2服务获取令牌时提供。
                .authorizedGrantTypes( // 设置授权类型列表
                        "refresh_token",
                        "password", // 支持密码授权类型（先要创建用户账号和密码）
                        "client_credentials") // 支持客户端凭据授权类型
                .scopes("webclient", "mobileclient"); // 设置应用在请求OAuth2服务器获取令牌时可以操作的范围。web端、手机端
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 使用spring提供的默认验证管理器 和用户详细信息服务
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
