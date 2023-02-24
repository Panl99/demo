package com.lp.demo.auth.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

//    多种认证方式时配置
//    @Bean
//    public PhoneRealm phoneRealm() {
//        return new PhoneRealm();
//    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     *
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

//    @Bean
//    public SecurityManager securityManager(CustomRealm customRealm, PhoneRealm phoneRealm, AbstractAuthenticator abstractAuthenticator) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置realms
//        List<Realm> realms = new ArrayList<>();
//        realms.add(customRealm);
//        realms.add(phoneRealm);
//        securityManager.setRealms(realms);
//        // 自定义缓存实现，可以使用redis
////        securityManager.setCacheManager(shiroCacheManager());
//        // 自定义session管理，可以使用redis
////        securityManager.setSessionManager(sessionManager());
//        // 注入记住我管理器
////        securityManager.setRememberMeManager(rememberMeManager());
//        // 认证器
//        securityManager.setAuthenticator(abstractAuthenticator);
//        return securityManager;
//    }
//
//    /**
//     * 认证器
//     */
//    @Bean
//    public AbstractAuthenticator abstractAuthenticator(CustomRealm customRealm, PhoneRealm phoneRealm){
//        // 自定义模块化认证器，用于解决多realm抛出异常问题
//        ModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
//        // 认证策略：AtLeastOneSuccessfulStrategy(默认)，AllSuccessfulStrategy，FirstSuccessfulStrategy
//        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//        List<Realm> realms = new ArrayList<>();
//        realms.add(customRealm);
//        realms.add(phoneRealm);
//        authenticator.setRealms(realms);
//        return authenticator;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 授权所用配置
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义未登录过滤器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new LoginFilter());
        shiroFilterFactoryBean.setFilters(filters);
        //对静态资源不拦截
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/*.png", "anon");
        map.put("/app/**", "anon");
        map.put("/doc/**", "anon");
        map.put("/file/**", "anon");
        //对不需要登录的接口不拦截
        map.put("/user/v1/login", "anon");
        map.put("/user/v1/captcha", "anon");
        map.put("/user/v1/checkCaptcha", "anon");
        map.put("/sms/verificationCode/v1", "anon");
        //对所有用户认证
        map.put("/**", "authc");
        //登出
        map.put("/user/v1/logout", "logout");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
}
