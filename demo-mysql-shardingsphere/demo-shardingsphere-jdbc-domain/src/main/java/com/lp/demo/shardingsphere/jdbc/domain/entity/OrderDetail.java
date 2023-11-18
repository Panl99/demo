package com.lp.demo.shardingsphere.jdbc.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1018275860211314546L;

    private Long id;

    private Long orderId;

    private Long tenantId;

    private String regionCode;

    private String address;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
