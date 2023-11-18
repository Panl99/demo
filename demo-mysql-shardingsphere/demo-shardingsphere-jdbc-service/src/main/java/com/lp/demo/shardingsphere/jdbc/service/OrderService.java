package com.lp.demo.shardingsphere.jdbc.service;

import cn.hutool.core.util.RandomUtil;
import com.lp.demo.shardingsphere.jdbc.common.result.CommonPage;
import com.lp.demo.shardingsphere.jdbc.domain.mapper.OrderMapper;
import com.lp.demo.shardingsphere.jdbc.domain.entity.Order;
import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderDetail;
import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderItem;
import com.lp.demo.shardingsphere.jdbc.domain.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisIdGeneratorService redisIdGeneratorService;

    @Transactional(rollbackFor = Exception.class)
    public long save() {
        Long tenantId = RandomUtil.randomLong(100, 999);
        String regionCode = "SZ";

        //保存订单基本信息
        Order order = new Order();
        Long orderId = redisIdGeneratorService.createUniqueId(String.valueOf(tenantId));
        order.setId(orderId);
        order.setTenantId(tenantId);
        order.setRegionCode(regionCode);
        order.setAmount(BigDecimal.valueOf(10.00));
        order.setMobile("132****8796");
        orderMapper.saveOrder(order);

        //保存订单详情
        OrderDetail orderDetail = new OrderDetail();
        Long detailId = redisIdGeneratorService.createUniqueId(String.valueOf(tenantId));
        orderDetail.setId(detailId);
        orderDetail.setOrderId(orderId);
        orderDetail.setTenantId(tenantId);
        orderDetail.setAddress("订单地址");
        orderDetail.setStatus(1);
        orderDetail.setRegionCode(regionCode);
        orderMapper.saveOrderDetail(orderDetail);

        //保存订单条目表
        //条目1
        OrderItem item1 = new OrderItem();
        Long itemId1 = redisIdGeneratorService.createUniqueId(String.valueOf(tenantId));
        item1.setId(itemId1);
        item1.setTenantId(tenantId);
        item1.setOrderId(orderId);
        item1.setRegionCode("BB");
        item1.setGoodId("b0001");
        item1.setGoodName("商品b0001");
        orderMapper.saveOrderItem(item1);
        //条目2
        OrderItem item2 = new OrderItem();
        Long itemId2 = redisIdGeneratorService.createUniqueId(String.valueOf(tenantId));
        item2.setId(itemId2);
        item2.setTenantId(tenantId);
        item2.setRegionCode("DD");
        item2.setOrderId(orderId);
        item2.setGoodId("d0001");
        item2.setGoodName("商品d0001");
        orderMapper.saveOrderItem(item2);

        return orderId;
    }


    public OrderVo get(long orderId) {
        OrderVo orderVo = orderMapper.selectOrder(orderId);
//        if (orderVo == null) {
//            return null;
//        }
//        OrderDetail orderDetail = orderMapper.selectOrderDetail(orderId, orderVo.getTenantId());
//        orderVo.setOrderDetail(orderDetail);
//        List<OrderItem> orderItems = orderMapper.selectOrderItems(orderId, orderVo.getTenantId());
//        orderVo.setOrderItems(orderItems);
        return orderVo;
    }

}
