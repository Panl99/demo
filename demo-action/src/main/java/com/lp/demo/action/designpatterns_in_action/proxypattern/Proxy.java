package com.lp.demo.action.designpatterns_in_action.proxypattern;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @create 2021/3/1 22:40
 * @auther outman
 * @description
 **/
@Slf4j
public class Proxy implements Company {
    private HR hr;

    public Proxy() {
        super();
        this.hr = new HR();
    }

    /**
     * 需要代理的方法
     * @param title
     */
    @Override
    public void findWorker(String title) {
        hr.findWorker(title);
        // 通过猎头找候选人
        String worker = getWorker(title);
        log.info("find a worker by proxy, worker is: {}", worker);
    }

    private String getWorker(String title) {
        Map<String, String> workerList = new HashMap<>();
        workerList.put("Java", "zhangsan");
        workerList.put("Python", "lisi");
        workerList.put("Go", "wangwu");
        return workerList.get(title);
    }
}
