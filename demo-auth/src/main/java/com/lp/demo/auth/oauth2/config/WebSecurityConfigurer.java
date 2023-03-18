package com.lp.demo.auth.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author lp
 * @date 2022/10/24 22:18
 * @desc 定义用户id、密码、角色
 **/
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("zhangsan")
                .password("123456")
                .roles("USER")
                .and()
                .withUser("lisi")
                .password("112233")
                .roles("USER", "ADMIN");
    }

    /**
     * 下边两个Bean：验证用户信息；并返回用户信息
     * 下边两个Bean被注入到OAuth2Config中的configure(AuthenticationServerEndpointsConfigurer endpoints)方法中
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
