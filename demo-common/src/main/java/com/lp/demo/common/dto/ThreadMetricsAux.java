package com.lp.demo.common.dto;

import lombok.Data;

/**
 * @author lp
 * @date 2021/8/27 10:12
 **/
@Data
public class ThreadMetricsAux {
    private long usedNanoTime;

    private long lastNanoTime;

    public ThreadMetricsAux() {
    }

    public ThreadMetricsAux(long usedNanoTime, long lastNanoTime) {
        this.usedNanoTime = usedNanoTime;
        this.lastNanoTime = lastNanoTime;
    }

}
