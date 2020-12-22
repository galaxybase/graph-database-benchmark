package com.galaxybase.benchmark.common.result.data;

import java.math.BigDecimal;

/**
 * 取平均值，保留两位小数
 */
public class AvgData implements ResultData<BigDecimal, BigDecimal> {
    private BigDecimal data = BigDecimal.valueOf(0);
    private int total;

    @Override
    public void addData(BigDecimal data) {
        this.data = this.data.add(data);
        total++;
    }

    @Override
    public BigDecimal getResult() {
        if (total == 0) {
            return BigDecimal.valueOf(0);
        }
        return data.divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String asString() {
        return String.format("Average: %.2f", getResult());
    }
}
