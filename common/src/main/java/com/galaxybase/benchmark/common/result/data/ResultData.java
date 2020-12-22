package com.galaxybase.benchmark.common.result.data;

/**
 * 测试结果输出一个项
 * @param <T> 测试存储数据类型
 */
public interface ResultData<T, V> {

    /**
     * 添加一项数据
     */
    void addData(T data);

    /**
     * 取得处理过的总的数据值，如平均值
     * @return 数据值
     */
    V getResult();

    /**
     * 以String格式输出结果
     * @return 结果输出
     */
    String asString();

}
