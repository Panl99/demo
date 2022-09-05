package com.lp.demo.auth.jwt.model;

import com.lp.demo.auth.jwt.util.JwtUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtUserVO extends JwtToken implements Serializable {

    private static final long serialVersionUID = -7704785035378158109L;

    private User user;

    public JwtUserVO() {
    }

    public JwtUserVO(User user, Integer expire) {
        JwtUserInfo jwtUserInfo = new JwtUserInfo(user);
        super.setToken(JwtUtil.createToken(jwtUserInfo, expire));
        super.setRefreshToken(JwtUtil.createRefToken(jwtUserInfo, expire * 2));
        this.user = user;
    }
}
