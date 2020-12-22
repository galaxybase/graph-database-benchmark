package com.galaxybase.benchmark.common.result.data;

/**
 * 概率记录器
 */
public class RatioData implements ResultData<Boolean, Double> {
    private int num;
    private int total;

    /**
     * 添加数据，记录True的比率
     *
     * @param data
     */
    @Override
    public void addData(Boolean data) {
        total++;
        num = data ? num++ : num;
    }

    @Override
    public Double getResult() {
        if (num == 0 || total == 0) {
            return 0.0;
        }
        return (double) num / total * 100;
    }

    @Override
    public String asString() {
        return String.format("Rate: %.2f%%", getResult());
    }
}
