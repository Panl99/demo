package com.lp.demo.xxljob.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.ResultEnum;
import com.lp.demo.xxljob.service.XxlJobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lp
 * @date 2021/9/8 11:16
 **/
@Service
public class XxlJobServiceImpl implements XxlJobService {

    @Value("${xxl.job.admin.host}")
    private String xxlJobAdminHost;

    private static final String JOB_INFO_URL = "/xxl-job-admin/jobinfo/";

    @Override
    public long addJob(String describe, String cron, String jobHandler, String param) {
        // 添加任务
        String response = HttpUtil.createPost(xxlJobAdminHost + JOB_INFO_URL + "add")
                .form("jobGroup", 2)
                .form("jobDesc", describe)
                .form("author", "")
                .form("scheduleType", "CRON")
                .form("scheduleConf", cron)
                .form("cronGen_display", cron)
                .form("glueType", "BEAN")
                .form("executorHandler", jobHandler)
                .form("executorParam", param)
                .form("executorRouteStrategy", "ROUND")
                .form("misfireStrategy", "DO_NOTHING")
                .form("executorBlockStrategy", "SERIAL_EXECUTION")
                .form("executorTimeout", 0)
                .form("executorFailRetryCount", 0)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer code = jsonObject.getInteger("code");
        if (ResultEnum.SUCCESS.getCode() != code) {
            throw new DisplayableException(500,"添加任务失败");
        }
        long jobId = Long.parseLong(jsonObject.getString("content"));
        // 启动任务
        startJob(jobId);
        return jobId;
    }

    @Override
    public void deleteJob(Long id) {
        String response = HttpUtil.createPost(xxlJobAdminHost + JOB_INFO_URL + "remove")
                .form("id", id)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer code = jsonObject.getInteger("code");
        if (ResultEnum.SUCCESS.getCode() != code) {
            throw new DisplayableException(500, "删除任务失败");
        }
    }

    @Override
    public void updateJob(Long id, String describe, String cron, String jobHandler, String param) {
        String response = HttpUtil.createPost(xxlJobAdminHost + JOB_INFO_URL + "update")
                .form("id", id)
                .form("jobGroup", 2)
                .form("jobDesc", describe)
                .form("author", "")
                .form("scheduleType", "CRON")
                .form("scheduleConf", cron)
                .form("cronGen_display", cron)
                .form("glueType", "BEAN")
                .form("executorHandler", jobHandler)
                .form("executorParam", param)
                .form("executorRouteStrategy", "ROUND")
                .form("misfireStrategy", "DO_NOTHING")
                .form("executorBlockStrategy", "SERIAL_EXECUTION")
                .form("executorTimeout", 0)
                .form("executorFailRetryCount", 0)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer code = jsonObject.getInteger("code");
        if (ResultEnum.SUCCESS.getCode() != code) {
            throw new DisplayableException(500, "修改任务失败");
        }
    }

    @Override
    public String showJobList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("jobGroup", jobGroup);
        paramMap.put("triggerStatus", triggerStatus);
        paramMap.put("jobDesc", jobDesc);
        paramMap.put("executorHandler", executorHandler);
        paramMap.put("author", author);
        return HttpUtil.get(xxlJobAdminHost + JOB_INFO_URL + "pageList", paramMap);
    }

    @Override
    public void startJob(long id) {
        String response = HttpUtil.createPost(xxlJobAdminHost + JOB_INFO_URL + "start")
                .form("id", (int) id)
                .execute()
                .body();
        System.out.println("response = "+ response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject == null || ResultEnum.SUCCESS.getCode() != jsonObject.getInteger("code")) {
            throw new DisplayableException(500, "启动任务失败");
        }
    }

    @Override
    public void stopJob(long id) {
        String response = HttpUtil.createPost(xxlJobAdminHost + JOB_INFO_URL + "stop")
                .form("id", id)
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject == null || ResultEnum.SUCCESS.getCode() != jsonObject.getInteger("code")) {
            throw new DisplayableException(500, "停止任务失败");
        }
    }

    @Override
    public void login(String username, String password, String ifRemember) {
        String response = HttpUtil.createPost(xxlJobAdminHost + "/xxl-job-admin/login")
                .form("userName", username)
                .form("password", password)
                .form("ifRemember", ifRemember)
                .execute()
                .body();
        System.out.println("response = " + response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject == null || ResultEnum.SUCCESS.getCode() != jsonObject.getInteger("code")) {
            throw new DisplayableException(500, "登录失败");
        }
    }
}
