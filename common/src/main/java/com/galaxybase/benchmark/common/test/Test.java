package com.galaxybase.benchmark.common.test;

import com.galaxybase.benchmark.common.result.Results;

public interface Test {
    /**
     * 取得当前测试项名称
     * @return 测试项名称
     */
    String getTestName();

    /**
     * 执行测试
     */
    void startTest();

    /**
     * 完成测试
     */
    void finishTest();

    /**
     * 设置执行次数
     */
    void setTotalNum(int num);

    /**
     * 设置样本
     * @param key
     */
    void setSample(String key);

    /**
     * 设置超时时间，单位为ms
     * @param timeout 超时时间
     */
    void setTimeout(long timeout);

    /**
     * 取得测试结果
     */
    Results getResults();
}
