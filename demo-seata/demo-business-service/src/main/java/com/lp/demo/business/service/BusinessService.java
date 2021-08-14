package com.lp.demo.business.service;


import com.lp.demo.business.dto.BusinessDTO;
import com.lp.demo.common.result.JsonResult;

public interface BusinessService {

    /**
     * 出处理业务服务
      * @param businessDTO
     * @return
     */
    JsonResult handleBusiness(BusinessDTO businessDTO);


    /**
     * 出处理业务服务，出现异常回顾
     * @param businessDTO
     * @return
     */
    JsonResult handleBusiness2(BusinessDTO businessDTO);
}
