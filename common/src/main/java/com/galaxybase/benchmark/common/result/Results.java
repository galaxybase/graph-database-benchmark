package com.galaxybase.benchmark.common.result;

import java.util.List;

/**
 * 用于存储测试结果输出
 */
public interface Results {
    /**
     * 测试项目名称
     *
     * @return
     */
    String getName();

    /**
     * 取得错误任务数
     *
     * @return
     */
    int getErrorCount();

    /**
     * 超时任务数
     *
     * @return
     */
    int getTimeoutCount();

    /**
     * 添加错误任务
     *
     * @param currentCount
     * @return
     */
    boolean addErrorCount(int currentCount);

    /**
     * 添加超时任务
     *
     * @param currentCount
     */
    void addTimeoutCount(int currentCount);

    /**
     * 添加执行时间
     *
     * @param time 执行时间
     */
    void addTime(long time);

    /**
     * 添加执行时间
     *
     * @param time 执行时间
     */
    void addTime(List<Long> time);

    /**
     * 取得运行时间列表
     *
     * @return
     */
    List<Long> getTimes();

    /**
     * 取得执行时间
     *
     * @return
     */
    long getRunTime();

    /**
     * 打印统计日志
     */
    void statistics();
}
