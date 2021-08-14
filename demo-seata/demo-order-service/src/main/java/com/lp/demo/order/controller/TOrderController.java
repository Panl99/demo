package com.lp.demo.order.controller;

import com.lp.demo.common.result.JsonResult;
import com.lp.demo.order.dto.OrderDTO;
import com.lp.demo.order.service.ITOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  订单服务
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class TOrderController {
    
    @Autowired
    private ITOrderService orderService;

    @PostMapping("/create")
    JsonResult<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO){
        log.info("请求订单微服务：{}",orderDTO.toString());
        return orderService.createOrder(orderDTO);
    }
}

