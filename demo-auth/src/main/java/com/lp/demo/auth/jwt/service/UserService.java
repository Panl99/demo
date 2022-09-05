package com.lp.demo.auth.jwt.service;

import com.lp.demo.auth.jwt.model.User;

/**
 * @author lp
 * @date 2022/9/5 12:02
 * @desc
 **/
public interface UserService {
    /**
     * 微信鉴权
     *
     * @param code   微信登录code
     * @param appId  微信应用id
     * @return userinfo
     */
    User wxOAuth(String code, String appId);

    /**
     * 微信鉴权
     *
     * @param code   微信登录code
     * @param appId  微信应用id
     * @param encryptedData  手机加密数据
     * @param iv  加密向量
     * @return userinfo
     */
    User wxOAuth(String code, String appId, String encryptedData, String iv);

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    User userInfo(Long id);
}
