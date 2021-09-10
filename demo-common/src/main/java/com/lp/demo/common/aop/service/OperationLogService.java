package com.lp.demo.common.aop.service;

public interface OperationLogService {

    void saveLogWithBackground(Long userId,
                               String operator,
                               String scene,
                               String request,
                               String response,
                               String remoteAddress);
}
