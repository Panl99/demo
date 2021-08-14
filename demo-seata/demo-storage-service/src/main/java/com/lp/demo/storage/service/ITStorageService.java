package com.lp.demo.storage.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.storage.dto.CommodityDTO;
import com.lp.demo.storage.entity.TStorage;

/**
 * 仓库服务
 */
public interface ITStorageService extends IService<TStorage> {

    /**
     * 扣减库存
     */
    JsonResult decreaseStorage(CommodityDTO commodityDTO);
}
