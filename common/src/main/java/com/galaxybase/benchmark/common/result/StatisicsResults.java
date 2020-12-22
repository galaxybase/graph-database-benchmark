package com.galaxybase.benchmark.common.result;

import com.galaxybase.benchmark.common.util.LoggerUtil;

import static com.galaxybase.benchmark.common.util.LoggerUtil.LOG;

public class StatisicsResults extends AbstractResults {

    public StatisicsResults(String name) {
        super(name);
        runTime = 0;
    }

    @Override
    public boolean addErrorCount(int currentCount) {
        errorCount += currentCount;
        return true;
    }

    /**
     * 添加runTime，累加
     * @param runTime
     */
    public void addRunTime(long runTime) {
        super.runTime += runTime;
    }

    @Override
    public void addTimeoutCount(int currentCount) {
        timeoutCount += currentCount;
    }

    @Override
    public long getRunTime() {
        return runTime;
    }

    @Override
    public void statistics() {
        LOG.info("***********************");
        LOG.info("** " + getName());
        LoggerUtil.STATISTICS_CSV.info(printTimes());
        LOG.info("***********************");
    }
}
