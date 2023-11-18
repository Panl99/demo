package com.lp.demo.shardingsphere.jdbc.domain.vo;

import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderDetail;
import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderVo implements Serializable {

    private static final long serialVersionUID = -4970220050111132143L;

    /**
     * 订单id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单详情
     */
    private OrderDetail orderDetail;

    /**
     * 订单条目
     */
    private List<OrderItem> orderItems;

}
