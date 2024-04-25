package com.lp.demo.main.controller;

import com.lp.demo.common.service.ThreadLocalService;
import com.lp.demo.common.util.hutool.ZipUtilDemo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @create 2020/12/26 23:32
 * @auther outman
 **/
@Api(value = "/main", tags = {"*/"})
@RestController
@Slf4j
@RequestMapping("/main")
public class MainController {

    @RequestMapping("/test")
    public String test() {
        log.info("start test");
        return "Hello test";
    }

    @ApiOperation(value = "", notes = "", httpMethod = "GET")
    @GetMapping("/testget")
    public String testGet() {
        log.info("start testGet");
        return "Hello testGet";
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "file", name = "file", value = "", required = true)
    })
    @ApiOperation(value = "", notes = "", httpMethod = "POST")
    @PostMapping("/upload")
    public List uploadTest(MultipartFile file) throws IOException {
        ThreadLocalService.getInstance().setValue(file.getOriginalFilename());
        return ZipUtilDemo.unzip(file.getInputStream());
    }
}
