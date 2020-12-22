package com.galaxybase.benchmark.common.result;

import com.galaxybase.benchmark.common.result.data.ResultData;
import com.galaxybase.benchmark.common.util.StringTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.galaxybase.benchmark.common.util.LoggerUtil.LOG;

public abstract class AbstractResults implements Results {
    private final String name;

    /**
     * 存储执行的时间列表
     */
    protected List<Long> times;
    /**
     * 自定义添加data数据
     */
    protected Map<String, ResultData> resultDatas;
    /**
     * 测试时记录开始测试时间
     * 测试结束后修改为测试执行时间
     */
    protected long runTime;
    /**
     * 执行出错的任务数
     */
    protected int errorCount;
    /**
     * 执行超时的任务数
     */
    protected int timeoutCount;

    private static final String STATISICS_FORMAT = "%s|%s|%.2f|%.2f|%.2f|%.2f|%.2f|%.2f|%d|%.2f%%|%d|%.2f%%|%d|%.2f%%|";


    public AbstractResults(String name) {
        this.name = name;
        times = Collections.synchronizedList(new ArrayList<>());
        resultDatas = new ConcurrentHashMap<>();
        runTime = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getTimeoutCount() {
        return timeoutCount;
    }

    @Override
    public void addTime(long time) {
        times.add(time);
    }

    @Override
    public void addTime(List<Long> time) {
        times.addAll(time);
    }

    @Override
    public List<Long> getTimes() {
        return times;
    }

    /**
     * 打印和时间相关的统计信息，响应csv格式的数据，因为csv格式数据还需要进一步装载
     * runTime 总的运行时间，如果并发不应该把每个线程的耗时相加
     * times  测试项列表，传入前需要先保证列表递增排序
     */
    protected String printTimes() {
        long minTime = 0;
        long maxTime = 0;
        long avgTime = 0;
        long p99Time = 0;
        long p999Time = 0;
        double secondNum = 0;
        int succeeNum = 0;
        double succeeRate = 0;
        int totalNum = errorCount + timeoutCount;
        if (times != null && times.size() != 0) {
            times.sort((a, b) -> a.compareTo(b));
            succeeNum = times.size();
            totalNum += succeeNum;
            minTime = times.get(0);
            LOG.info("minTime: " + StringTool.getDetailNsTime(minTime));
            maxTime = times.get(succeeNum - 1);
            LOG.info("maxTime: " + StringTool.getDetailNsTime(maxTime));
            long totalTime = times.stream().mapToLong(v -> v).sum();
            avgTime = totalTime / succeeNum;
            LOG.info("AvgTime: " + StringTool.getDetailNsTime(avgTime));
            p99Time = (long) quantileNSub(0.99, times);
            LOG.info("P99: " + StringTool.getDetailNsTime(p99Time));
            p999Time = (long) quantileNSub(0.999, times);
            LOG.info("P999: " + StringTool.getDetailNsTime(p999Time));
            secondNum = (double) succeeNum * 1000 / runTime;
            // 平均每秒完成请求数
            LOG.info(String.format("Throughput: %.2f", secondNum));
            LOG.info("SuccessNum: " + succeeNum);
            succeeRate = (double) succeeNum * 100 / totalNum;
            LOG.info(String.format("SuccessRate: %.2f%%", succeeRate));
        } else {
            LOG.info("Results is null.");
        }
        double errorRate = 0;
        double timeoutRate = 0;
        if (totalNum != 0) {
            errorRate = (double) errorCount * 100 / totalNum;
            timeoutRate = (double) timeoutCount * 100 / totalNum;
            LOG.info("ErrorNum: " + errorCount);
            LOG.info(String.format("ErrorRate: %.2f%%", errorRate));
            LOG.info("TimeoutNum: " + timeoutCount);
            LOG.info(String.format("TimeoutRate: %.2f%%", timeoutRate));
        }
        return String.format(STATISICS_FORMAT,
                StringTool.presentTime(),
                StringTool.splitDispose(getName()),
                minTime / 1000000.0,
                maxTime / 1000000.0,
                avgTime / 1000000.0,
                p99Time / 1000000.0,
                p999Time / 1000000.0,
                secondNum,
                succeeNum, succeeRate, errorCount, errorRate, timeoutCount, timeoutRate);
    }

    /**
     * 计算分位值，N-1法
     *
     * @param n    分位数，大于0，小于1
     * @param list 待求分位值的列表，需要先排序好
     * @return 分位值
     */
    public static double quantileNSub(double n, List<Long> list) {
        if (n <= 0 || n >= 1) {
            throw new RuntimeException("Quantile value n: " + n);
        }
        double position = (list.size() - 1) * n + 1;
        int m = (int) position;
        if (m <= 0 || list.size() == 1) {
            return list.get(0);
        }
        long a = list.get(m);
        long b = list.get(m - 1);
        return b + (position - m) * (a - b);
    }
}
