package com.lp.demo.brcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lp
 * @date 2021/6/30 17:06
 **/
@RestController
@RequestMapping(value = "rcc")
@Slf4j
public class ConfigCenterController {

    @GetMapping(value = "getConfig")
    public void getConfigTest(HttpServletRequest request) {
        log.info("getConfigTest info");
        log.debug("getConfigTest debug");
        log.warn("getConfigTest warn");
        log.error("getConfigTest error");
        System.out.println("getConfigTest---end "+ request.getRequestURI());
    }
}
