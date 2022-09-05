package com.lp.demo.auth.jwt.service.impl;

import com.lp.demo.auth.jwt.model.User;
import com.lp.demo.auth.jwt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022/9/5 12:05
 * @desc
 **/
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User wxOAuth(String code, String appId) {
        return null;
    }

    @Override
    public User wxOAuth(String code, String appId, String encryptedData, String iv) {
        return null;
    }

    @Override
    public User userInfo(Long id) {
        return null;
    }
}
