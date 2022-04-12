package com.lp.demo.action.designpatterns_in_action.proxypattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @create 2021/3/1 22:37
 * @auther outman
 * @description
 **/
@Slf4j
public class HR implements Company {

    @Override
    public void findWorker(String title) {
        log.info("i need find a worker, title is: {}", title);
    }
}
