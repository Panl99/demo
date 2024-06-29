package com.lp.demo.main.controller;

import com.lp.demo.common.aop.annotation.MaskLog;
import com.lp.demo.common.aop.annotation.SaveLog;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.ConsoleColorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @SaveLog(scene = "新增项目用户",
            paramsIdxes = {0, 1, 2},
            params = {"productId", "projectId", "user"})
    @PostMapping("add/{productId}/{projectId}")
    public String addUser(@PathVariable Long productId,
                          @PathVariable Long projectId,
                          @RequestBody UserDto user) {
        ConsoleColorUtil.printDefaultColor("add user : productId = " + productId + ", projectId = " + projectId + ", user = " + user);
        return "add user success";
    }

    @SaveLog(scene = "修改用户",
            paramsIdxes = {0},
            params = {"user"},
            masks = @MaskLog(paramsIdx = 0,
                    fields = "phoneNumber",
                    maskLevel = MaskLog.MaskLevelEnum.PART
            )
    )
    @PutMapping({"update"})
    public String updateUser(@RequestBody UserDto user) {
        ConsoleColorUtil.printDefaultColor("update user : " + user);
        return "update user success";
    }

    @SaveLog(scene = "删除项目用户",
            paramsIdxes = {0, 1, 2},
            params = {"productId", "projectId", "names"})
    @PostMapping("del/{productId}/{projectId}")
    public String delMember(@PathVariable Long productId,
                       @PathVariable Long projectId,
                       @RequestBody List<String> names) {
        ConsoleColorUtil.printDefaultColor("del user : productId = " + productId + ", projectId = " + projectId + ", names = " + names);
        return "del user success";
    }

}
