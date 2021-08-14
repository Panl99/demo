package com.lp.demo.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单信息
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -8062162688031892710L;

    private String orderNo;

    private String userId;

    private String commodityCode;

    private Integer orderCount;

    private BigDecimal orderAmount;

}
