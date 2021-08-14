package com.lp.demo.storage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.storage.dto.CommodityDTO;
import com.lp.demo.storage.entity.TStorage;
import com.lp.demo.storage.mapper.TStorageMapper;
import com.lp.demo.storage.service.ITStorageService;
import org.springframework.stereotype.Service;

/**
 *  库存服务实现类
 */
@Service
public class TStorageServiceImpl extends ServiceImpl<TStorageMapper, TStorage> implements ITStorageService {

    @Override
    public JsonResult decreaseStorage(CommodityDTO commodityDTO) {
        int storage = baseMapper.decreaseStorage(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        JsonResult<Object> response = new JsonResult<>();
        if (storage > 0){
            return new JsonResult<>(200, "success");
        }

        return new JsonResult<>(999, "fail");
    }
}
