package com.lp.demo.storage.service;


import com.lp.demo.common.result.JsonResult;
import com.lp.demo.storage.dto.CommodityDTO;

/**
 * 库存服务
 */
public interface StorageDubboService {

    /**
     * 扣减库存
     */
    JsonResult decreaseStorage(CommodityDTO commodityDTO);
}
