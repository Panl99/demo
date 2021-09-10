package com.lp.demo.common.aop.service.impl;


import com.lp.demo.common.aop.entity.OperationLog;
import com.lp.demo.common.aop.service.OperationLogService;
import com.lp.demo.common.aop.thread.ThreadPoolUtil;
import com.lp.demo.common.util.ConsoleColorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Date;

@Slf4j
@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

    @Override
    public void saveLogWithBackground(Long userId, String operator, String scene, String request, String response, String remoteAddress) {
        ThreadPoolUtil.submitTask(() -> {
            long start = System.currentTimeMillis();
            OperationLog operationLog = OperationLog.newBuilder()
                    .userId(userId)
                    .operator(operator)
                    .scene(scene)
                    .request(request)
                    .response(response)
                    .createTime(new Date())
                    .remoteAddress(remoteAddress)
                    .build();
            try {
//                operationLogMapper.insertSelective(operationLog);
                ConsoleColorUtil.printDefaultColor("operationLogMapper.insertSelective(log); log="+operationLog);
                if (log.isDebugEnabled()) {
                    log.debug("saveLogWithBackground {} cast {}", operationLog, System.currentTimeMillis() - start);
                }
            } catch (Throwable ex) {
                log.error("saveLogWithBackground error. {}", operationLog, ex);
            }
        });
    }

    @PreDestroy
    public void stop() {
        ThreadPoolUtil.stop();
    }
}
