package com.lp.demo.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.demo.account.dto.AccountDTO;
import com.lp.demo.account.entity.TAccount;
import com.lp.demo.account.mapper.TAccountMapper;
import com.lp.demo.account.service.ITAccountService;
import com.lp.demo.common.result.JsonResult;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 */
@Service
public class TAccountServiceImpl extends ServiceImpl<TAccountMapper, TAccount> implements ITAccountService {

    @Override
    public JsonResult<AccountDTO> decreaseAccount(AccountDTO accountDTO) {
        int account = baseMapper.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount().doubleValue());
        if (account > 0){
            return new JsonResult(200, "success");
        }
        return new JsonResult(999, "fail");
    }
}
