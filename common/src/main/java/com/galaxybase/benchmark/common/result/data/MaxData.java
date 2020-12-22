package com.galaxybase.benchmark.common.result.data;

/**
 * 处理最大值
 *
 * @param <T> 数字类型
 */
public class MaxData<T extends Number> implements ResultData<T, T> {
    private T data;

    @Override
    public void addData(T data) {
        if (this.data == null) {
            this.data = data;
            return;
        }
        this.data = this.data.doubleValue() < data.doubleValue() ? data : this.data;
    }

    @Override
    public T getResult() {
        return data;
    }

    @Override
    public String asString() {
        return String.format("Max: %s", getResult());
    }
}
