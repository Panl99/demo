package com.lp.demo.business.service.impl;

import com.lp.demo.business.dto.BusinessDTO;
import com.lp.demo.business.service.BusinessService;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.order.dto.OrderDTO;
import com.lp.demo.order.service.OrderDubboService;
import com.lp.demo.storage.dto.CommodityDTO;
import com.lp.demo.storage.service.StorageDubboService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * Dubbo业务发起方逻辑
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @DubboReference(version = "1.0.0")
    private StorageDubboService storageDubboService;

    @DubboReference(version = "1.0.0")
    private OrderDubboService orderDubboService;

    boolean flag;

    /**
     * 处理业务逻辑 正常的业务逻辑
     * @Param:
     * @Return:
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public JsonResult handleBusiness(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = " + RootContext.getXID());
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        JsonResult storageResponse = storageDubboService.decreaseStorage(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        JsonResult<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        if (storageResponse.getCode() != 200 || response.getCode() != 200) {
            throw new RuntimeException("failed");
        }

        return new JsonResult(200, "success", response.getData());
    }

    /**
     * 出处理业务服务，出现异常回顾
     *
     * @param businessDTO
     * @return
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public JsonResult handleBusiness2(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = " + RootContext.getXID());
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        JsonResult storageResponse = storageDubboService.decreaseStorage(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        JsonResult<OrderDTO> response = orderDubboService.createOrder(orderDTO);

//        打开注释测试事务发生异常后，全局回滚功能
        if (!flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }

        if (storageResponse.getCode() != 200 || response.getCode() != 200) {
            throw new RuntimeException("failed");
        }

        return new JsonResult(200, "success", response.getData());
    }
}
