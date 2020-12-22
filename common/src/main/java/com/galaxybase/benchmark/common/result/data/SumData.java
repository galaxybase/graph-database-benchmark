package com.galaxybase.benchmark.common.result.data;

import java.math.BigDecimal;

/**
 * 和操作
 */
public class SumData implements ResultData<BigDecimal, BigDecimal> {
    private BigDecimal data = BigDecimal.valueOf(0);

    @Override
    public void addData(BigDecimal data) {
        this.data = this.data.add(data);
    }

    @Override
    public BigDecimal getResult() {
        return data;
    }

    @Override
    public String asString() {
        return String.format("Sum: %s", getResult());
    }
}
