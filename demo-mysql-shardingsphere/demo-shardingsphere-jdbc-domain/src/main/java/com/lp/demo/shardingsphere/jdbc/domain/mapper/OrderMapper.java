package com.lp.demo.shardingsphere.jdbc.domain.mapper;

import com.lp.demo.shardingsphere.jdbc.domain.entity.Order;
import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderDetail;
import com.lp.demo.shardingsphere.jdbc.domain.entity.OrderItem;
import com.lp.demo.shardingsphere.jdbc.domain.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    void saveOrder(Order order);

    void saveOrderDetail(OrderDetail orderDetail);

    void saveOrderItem(OrderItem orderItem);

    OrderVo selectOrder(Long orderId);

    OrderDetail selectOrderDetail(@Param("orderId") Long orderId, @Param("tenantId") Long tenantId);

    List<OrderItem> selectOrderItems(@Param("orderId") Long orderId, @Param("tenantId") Long tenantId);

}
