package com.galaxybase.benchmark.common.util;

import com.galaxybase.benchmark.common.result.Results;
import com.galaxybase.benchmark.common.result.StatisicsResults;
import com.galaxybase.benchmark.common.test.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试入口
 */
public final class TestManager {

    /**
     * 初始化操作，开始进行测试
     */
    public static void startTest() {
        TestConfiguration conf = TestConfiguration.initConfiguation();
        LoggerUtil.initLog(conf);
        LoggerUtil.LOG.info("Start Test!");
        try {
            doStartTest(conf.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerUtil.LOG.info("Test Success!");
    }

    /**
     * 开始测试
     *
     * @param items
     */
    private static void doStartTest(List<Map<String, Object>> items) throws Exception {
        if (items == null) {
            return;
        }
        Map<String, List<Results>> statistics = new HashMap<>();
        for (int t = 0; t < items.size(); t++) {
            Map<String, Object> item = items.get(t);
            String className = String.valueOf(item.get("testClass"));
            Class clazz = Class.forName(className);
            int loop = (int) item.getOrDefault("loop", 1);
            int count = (int) item.get("count");
            int timeout = (int) item.getOrDefault("timeout", 0) * 1000;
            String sample = (String) item.get("sample");
            String testName = null;
            for (int i = 0; i < loop; i++) {
                Test test = (Test) clazz.newInstance();
                test.setTotalNum(count);
                test.setSample(sample);
                if (timeout != 0) {
                    test.setTimeout(timeout);
                }
                testName = test.getTestName();
                for (int j = 0; j < count; j++) {
                    test.startTest();
                }
                test.finishTest();
                List<Results> results = statistics.get(testName);
                if (results == null) {
                    results = new ArrayList<>();
                    statistics.put(testName, results);
                }
                results.add(test.getResults());
            }
            // 指示当前是不是最后一项测试
            boolean flag = true;
            for (int i = t + 1; i < items.size(); i++) {
                if (String.valueOf(items.get(i).get("testClass")).equals(className)) {
                    flag = false;
                    break;
                }
            }
            // 当前是当前测试项最后一次执行，可以输出测试项测试的平均值了
            if (flag) {
                List<Results> results = statistics.get(testName);
                if (results.size() > 1) {
                    String name = String.format("Summary of %s Test 2 - %d", testName, results.size());
                    StatisicsResults statisicsResults = new StatisicsResults(name);
                    for (int i = 1; i < results.size(); i++) {
                        Results r = results.get(i);
                        statisicsResults.addRunTime(r.getRunTime());
                        statisicsResults.addTime(r.getTimes());
                        statisicsResults.addErrorCount(r.getErrorCount());
                        statisicsResults.addTimeoutCount(r.getTimeoutCount());
                    }
                    statisicsResults.statistics();
                }
            }
        }
        DataBaseFactory.close();
    }
}
