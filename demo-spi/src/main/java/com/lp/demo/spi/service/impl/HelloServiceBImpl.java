package com.lp.demo.spi.service.impl;

import com.lp.demo.spi.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022/8/20 22:46
 * @desc
 **/
@Service
public class HelloServiceBImpl implements HelloService {
    @Override
    public void hello() {
        System.out.println("hello B");
    }
}
