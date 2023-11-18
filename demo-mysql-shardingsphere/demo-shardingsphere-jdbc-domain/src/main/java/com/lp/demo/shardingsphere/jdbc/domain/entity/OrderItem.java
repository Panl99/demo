package com.lp.demo.shardingsphere.jdbc.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 4939743956074881084L;

    private Long id;

    private Long tenantId;

    private Long orderId;

    private String regionCode;

    private String goodId;

    private String goodName;

    private Date createTime;

    private Date updateTime;
}
