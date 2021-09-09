package com.lp.demo.xxljob.controller;

import com.lp.demo.common.result.JsonResult;
import com.lp.demo.common.result.ResultEnum;
import com.lp.demo.xxljob.model.JobInfo;
import com.lp.demo.xxljob.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lp
 * @date 2021/9/8 15:49
 *
 * 登录接口可以通
 * 其他接口不通，可能找不到服务，需配置注册中心
 *
 **/
@RestController
@RequestMapping("job")
public class XxlJobController {

    @Autowired
    XxlJobService jobService;

    @PostMapping(value = {"add", "insert"}, produces = {"application/json;charset=UTF-8"})
    public JsonResult<Long> addJob(@RequestBody JobInfo jobInfo) {
        long jobId = jobService.addJob(jobInfo.getJobDesc(), jobInfo.getScheduleConf(), jobInfo.getExecutorHandler(), jobInfo.getExecutorParam());
        return new JsonResult<>(ResultEnum.SUCCESS, jobId);
    }

    @DeleteMapping({"delete/{id}", "remove/{id}"})
    public JsonResult deleteJob(@PathVariable long id) {
        jobService.deleteJob(id);
        return new JsonResult<>(ResultEnum.SUCCESS);
    }

    @PutMapping({"update", "refresh"})
    public JsonResult updateJob(@RequestBody JobInfo jobInfo) {
        jobService.updateJob((long) jobInfo.getId(), jobInfo.getJobDesc(), jobInfo.getScheduleConf(), jobInfo.getExecutorHandler(), jobInfo.getExecutorParam());
        return new JsonResult<>(ResultEnum.SUCCESS);
    }

    @GetMapping({"show", "list"})
    public JsonResult<String> jobList(@RequestBody JobInfo jobInfo,
                                      @RequestParam(required = false, defaultValue = "0") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length) {
        String jobList = jobService.showJobList(start, length, jobInfo.getJobGroup(), jobInfo.getTriggerStatus(), jobInfo.getJobDesc(), jobInfo.getExecutorHandler(), jobInfo.getAuthor());
        return new JsonResult<>(ResultEnum.SUCCESS, jobList);
    }

    @RequestMapping({"start", "run", "execute", "resume"})
    public JsonResult startJob(@RequestParam long id) {
        jobService.startJob(id);
        return new JsonResult<>(ResultEnum.SUCCESS);
    }

    @RequestMapping({"stop", "pause"})
    public JsonResult stopJob(@RequestParam long id) {
        jobService.stopJob(id);
        return new JsonResult<>(ResultEnum.SUCCESS);
    }

    @PostMapping({"login"})
    public JsonResult login(@RequestParam String userName,
                            @RequestParam String password,
                            @RequestParam(required = false, defaultValue = "on") String ifRemember) {
        jobService.login(userName, password, ifRemember);
        return new JsonResult<>(ResultEnum.SUCCESS);
    }
}
