package com.galaxybase.benchmark.common.result;

import com.galaxybase.benchmark.common.result.data.ResultData;
import com.galaxybase.benchmark.common.util.StringTool;

import java.util.Map;
import java.util.stream.Collectors;

import static com.galaxybase.benchmark.common.util.LoggerUtil.*;

public class TestResults extends AbstractResults {

    /**
     * 当前超时的测试项的序号
     * 当执行超时时进行判断，如果等于该项目
     */
    private int currentTimeoutCount;

    public TestResults(String name) {
        super(name);
    }

    /**
     * 需要传入当前测试项的序号，如果当前测试项等于超时测试项的话，
     * 就认为是因为任务超时强制关闭测试导致任务项执行出错，就不计算为执行错误
     *
     * @param currentCount 当前测试项
     * @return true:当前测试项不是因为任务超时，false反之
     */
    @Override
    public boolean addErrorCount(int currentCount) {
        if (currentCount != currentTimeoutCount) {
            errorCount++;
            return true;
        }
        return false;
    }


    @Override
    public void addTimeoutCount(int currentCount) {
        currentTimeoutCount = currentCount;
        timeoutCount++;
    }

    public Map<String, ResultData> getResultDatas() {
        return resultDatas;
    }

    /**
     * 添加一个数据
     *
     * @param key  数据名
     * @param data 数据内容
     */
    public void addData(String key, ResultData data) {
        ResultData resultData = resultDatas.get(key);
        if (resultData == null) {
            resultDatas.put(key, data);
        } else {
            resultData.addData(data);
        }
    }

    /**
     * 取得自定义添加的data
     *
     * @param key data名称
     * @return
     */
    public <T extends ResultData> T getData(String key) {
        return (T) resultDatas.get(key);
    }

    /**
     * 结束测试
     */
    public void endTest() {
        runTime = System.currentTimeMillis() - runTime;
        // 时间从小到大排序
        statistics();
    }

    @Override
    public long getRunTime() {
        return runTime;
    }

    /**
     * 打印统计日志
     */
    @Override
    public void statistics() {
        LOG.info("-- Result Statistics");
        String result = printTimes() + resultDatas.entrySet().stream().map(entry -> {
            String s = String.format("%s %s", entry.getKey(), entry.getValue().asString());
            LOG.info(s);
            return StringTool.splitDispose(s);
        }).collect(Collectors.joining("; "));
        STATISTICS_CSV.info(result);
    }
}
