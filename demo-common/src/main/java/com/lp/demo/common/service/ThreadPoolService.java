package com.lp.demo.common.service;

/**
 * @author lp
 * @date 2021/10/20 14:39
 **/
public interface ThreadPoolService<Job extends Runnable> {
    /**
     * 执行Job
     * @param job
     */
    void execute(Job job);

    /**
     * 关闭线程池
     */
    void sutdown();

    /**
     * 添加工作者线程(重复执行Job的线程)
     * @param num
     */
    void addWorkers(int num);

    /**
     * 移除工作者线程
     * @param num
     */
    void removeWorkers(int num);

    /**
     * 获取正在等待执行的任务个数
     * @return
     */
    int getJobSize();

}
