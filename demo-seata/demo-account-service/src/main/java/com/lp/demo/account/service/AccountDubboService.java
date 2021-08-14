package com.lp.demo.account.service;


import com.lp.demo.account.dto.AccountDTO;
import com.lp.demo.common.result.JsonResult;

/**
 * 账户服务接口
 */
public interface AccountDubboService {

    /**
     * 从账户扣钱
     */
    JsonResult decreaseAccount(AccountDTO accountDTO);
}
