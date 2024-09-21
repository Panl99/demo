package com.lp.demo.spring.transaction;


import org.springframework.transaction.annotation.Transactional;

public class TransactionManageServiceTest {

    @Transactional
    public void executeWithTransaction() {
        // 执行一些事务操作...

        // 注册事务提交后的操作
        TransactionManageService.afterCommit(() -> System.out.println("Transaction committed successfully!"));

        // 注册事务回滚后的操作
        TransactionManageService.afterRollback(() -> System.out.println("Transaction rolledback!"));
    }


}
