package com.lp.demo.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.order.dto.OrderDTO;
import com.lp.demo.order.entity.TOrder;

/**
 * 创建订单
 */
public interface ITOrderService extends IService<TOrder> {

    /**
     * 创建订单
     */
    JsonResult<OrderDTO> createOrder(OrderDTO orderDTO);
}
