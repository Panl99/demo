package com.lp.demo.order.service;


import com.lp.demo.common.result.JsonResult;
import com.lp.demo.order.dto.OrderDTO;

/**
 * 订单服务接口
 */
public interface OrderDubboService {

    /**
     * 创建订单
     */
    JsonResult createOrder(OrderDTO orderDTO);
}
