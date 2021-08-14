package com.lp.demo.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户信息
 */
@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 3770268452875356300L;

    private Integer id;

    private String userId;

    private BigDecimal amount;

}
