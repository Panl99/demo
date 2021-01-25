package com.outman.demomain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create 2020/12/28 22:58
 * @auther outman
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/get")
    public String test() {
        log.info("start test");
        return "Hello test";
    }

    @GetMapping("/testget")
    public String testGet() {
        log.info("start testGet");
        return "Hello testGet";
    }
}
