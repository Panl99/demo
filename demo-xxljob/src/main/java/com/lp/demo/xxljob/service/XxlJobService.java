package com.lp.demo.xxljob.service;

/**
 * @author lp
 * @date 2021/9/8 11:15
 **/
public interface XxlJobService {
    /**
     * 添加定时任务
     *
     * @param describe   描述
     * @param cron       cron表达式
     * @param jobHandler 处理器名称
     * @param param      执行参数，长度限制512
     * @return 任务id
     */
    long addJob(String describe, String cron, String jobHandler, String param);

    /**
     * 删除定时任务
     *
     * @param id 任务id
     */
    void deleteJob(Long id);

    /**
     * 修改定时任务
     *
     * @param id         任务id
     * @param describe   描述
     * @param cron       cron表达式
     * @param jobHandler 处理器名称
     * @param param      执行参数，长度限制512
     */
    void updateJob(Long id, String describe, String cron, String jobHandler, String param);

    /**
     * 定时任务列表
     *
     * @param start 开始行
     * @param length 个数
     * @param jobGroup 执行器主键ID
     * @param triggerStatus 调度状态：0-停止，1-运行
     * @param jobDesc 任务描述
     * @param executorHandler 执行器任务handler
     * @param author 用户
     * @return
     */
    String showJobList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 启动任务
     * @param id 任务id
     */
    void startJob(long id);

    /**
     * 停止任务
     * @param id 任务id
     */
    void stopJob(long id);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param ifRemember 是否记住
     */
    void login(String username, String password, String ifRemember);
}
