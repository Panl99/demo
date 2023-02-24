package com.lp.demo.auth.shiro;

import com.lp.demo.auth.shiro.component.SessionContext;
import com.lp.demo.auth.shiro.entity.AdminPermission;
import com.lp.demo.auth.shiro.entity.AdminRole;
import com.lp.demo.auth.shiro.entity.AdminUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomRealm extends AuthorizingRealm {

//    @Autowired
//    private LoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户对象
        AdminUser adminUser = (AdminUser) (SessionContext.getSessionAttr(SessionContext.ADMIN_USER));
        // 添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (AdminRole adminRole : adminUser.getRoles()) {
            // 添加角色
            simpleAuthorizationInfo.addRole(adminRole.getRoleName());
            // 添加权限
            for (AdminPermission adminPermission : adminRole.getPermissions()) {
                if (adminPermission.getType() == 1) {
                    simpleAuthorizationInfo.addStringPermission(adminPermission.getPermissionName());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String principal = (String) authenticationToken.getPrincipal();
        AdminUser adminUser = new AdminUser();// TODO 查询用户 loginService.getAdminUserByAccount(principal);
        if (adminUser == null) {
            //这里返回后会报出对应异常
            return null;
        }

        String credentials;
        if (authenticationToken instanceof UsernamePasswordToken) {
            credentials = adminUser.getPassword();
        } else if (authenticationToken instanceof VerificationCodeToken) {
            credentials = principal;
        } else {
            return null;
        }

        //将用户对象存入session中
        SessionContext.setSessionAttr(SessionContext.ADMIN_USER, adminUser);
        //这里验证authenticationToken和simpleAuthenticationInfo的信息
        return new SimpleAuthenticationInfo(principal, credentials, getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken ||
                token instanceof VerificationCodeToken;
    }

    /**
     * 设置密码匹配器，自定义密码比对
     */
    //    @Override
//    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//        super.setCredentialsMatcher(new CustomCredentialsMatcher());
//    }
}
