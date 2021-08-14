package com.lp.demo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lp.demo.account.entity.TAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TAccountMapper extends BaseMapper<TAccount> {

    /**
     * 减少账户余额
     * @param userId
     * @param amount
     * @return
     */
    int decreaseAccount(@Param("userId") String userId, @Param("amount") Double amount);
}
