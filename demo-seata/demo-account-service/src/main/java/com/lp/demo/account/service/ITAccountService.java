package com.lp.demo.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.demo.account.dto.AccountDTO;
import com.lp.demo.account.entity.TAccount;
import com.lp.demo.common.result.JsonResult;

/**
 *  服务类
 */
public interface ITAccountService extends IService<TAccount> {

    /**
     * 扣用户钱
     */
    JsonResult<AccountDTO> decreaseAccount(AccountDTO accountDTO);
}
