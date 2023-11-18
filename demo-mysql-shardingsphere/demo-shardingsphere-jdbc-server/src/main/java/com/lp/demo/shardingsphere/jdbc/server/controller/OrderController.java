package com.lp.demo.shardingsphere.jdbc.server.controller;

import com.lp.demo.shardingsphere.jdbc.common.result.R;
import com.lp.demo.shardingsphere.jdbc.domain.vo.OrderVo;
import com.lp.demo.shardingsphere.jdbc.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "测试接口")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/save")
    @ApiOperation("save")
    public R<Long> save() {
        return R.success(orderService.save());
    }

    @GetMapping("/get")
    @ApiOperation("get")
    public R<OrderVo> get(long orderId) {
        return R.success(orderService.get(orderId));
    }

}
