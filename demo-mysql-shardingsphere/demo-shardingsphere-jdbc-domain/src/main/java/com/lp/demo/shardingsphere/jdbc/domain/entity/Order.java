package com.lp.demo.shardingsphere.jdbc.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {

    private static final long serialVersionUID = -1670540249338890503L;

    private Long id;

    private Long tenantId;

    private String regionCode;

    private BigDecimal amount;

    private String mobile;

    private Date createTime;

    private Date updateTime;

}
