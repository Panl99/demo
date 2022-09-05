package com.lp.demo.auth.jwt.model;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtToken implements Serializable {

    private static final long serialVersionUID = 9153582084521031797L;

    private String token;

    private String refreshToken;
}
