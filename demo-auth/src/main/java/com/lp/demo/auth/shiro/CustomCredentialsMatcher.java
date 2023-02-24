package com.lp.demo.auth.shiro;
//
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.credential.CredentialsMatcher;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
// * @author lp
// * @date 2023/2/21 14:26
// * @desc 自定义密码匹配规则
// **/
//public class CustomCredentialsMatcher implements CredentialsMatcher {
//
//    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Override
//    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
//        //前端传来的密码
//        String currentPass = String.copyValueOf((char[]) authenticationToken.getCredentials());
//        //数据库密码
//        String dbPass = (String) authenticationInfo.getCredentials();
//        return passwordEncoder.matches(currentPass, dbPass);
//    }
//}
