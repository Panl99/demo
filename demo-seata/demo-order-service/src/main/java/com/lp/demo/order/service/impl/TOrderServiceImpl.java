package com.lp.demo.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.demo.account.dto.AccountDTO;
import com.lp.demo.account.service.AccountDubboService;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.order.dto.OrderDTO;
import com.lp.demo.order.entity.TOrder;
import com.lp.demo.order.mapper.TOrderMapper;
import com.lp.demo.order.service.ITOrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *  服务实现类
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @DubboReference(version = "1.0.0")
    private AccountDubboService accountDubboService;

    /**
     * 创建订单
     * @Param:  OrderDTO  订单对象
     * @Return:  OrderDTO  订单对象
     */
    @Override
    public JsonResult<OrderDTO> createOrder(OrderDTO orderDTO) {
        JsonResult<OrderDTO> response = new JsonResult<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        JsonResult objectResponse = accountDubboService.decreaseAccount(accountDTO);

        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        //生成订单
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(orderDTO,tOrder);
        tOrder.setCount(orderDTO.getOrderCount());
        tOrder.setAmount(orderDTO.getOrderAmount().doubleValue());
        try {
            baseMapper.createOrder(tOrder);
        } catch (Exception e) {
            return new JsonResult<>(999, "fail");
        }

        if (objectResponse.getCode() != 200) {
            return new JsonResult<>(999, "fail");
        }

        return new JsonResult<>(200, "success");
    }
}
