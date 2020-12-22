package com.galaxybase.benchmark.common.test;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.result.TestResults;
import com.galaxybase.benchmark.common.util.DataBaseFactory;
import com.galaxybase.benchmark.common.util.LoggerUtil;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * 关于测试项的封装，每一个测试项都必须继承本抽象类
 *
 * @param <T> 数据库连接类
 * @param <V> 测试结果类
 */
public abstract class AbstractTest<T, V> implements Test {
    protected static final Logger LOG = LoggerUtil.LOG;
    protected DataBase<T> dataBase = DataBaseFactory.getDataBase();
    protected AtomicInteger count;
    /**
     * 当前测试项的样本
     */
    private Sample sample;
    private int totalNum;
    /**
     * 线程池，每个测试项一个线程，当有测试项无法完成时就可以关闭该测试项对应的线程
     */
    protected ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 超时时间，单位ns
     */
    private long timeout;
    private boolean isStop;
    /**
     * 遇到错误是否停止测试
     */
    private final boolean errorStop;
    /**
     * 测试结果统计
     */
    private TestResults results;
    /**
     * 超时是否停止测试项目
     */
    private final boolean timeoutStop;

    public AbstractTest() {
        TestConfiguration conf = TestConfiguration.instance();
        sample = conf.getSample();
        setTimeout(conf.getTimeout());
        timeout = (long) conf.getTimeout() * 1000000;
        errorStop = conf.isErrorStop();
        timeoutStop = conf.isTimeoutStop();
        count = new AtomicInteger(0);
        onInitTest();
        LOG.info("-- Start " + getTestName());
        results = new TestResults(getTestName());
    }

    @Override
    public void startTest() {
        if (isStop) {
            return;
        }
        int n = count.incrementAndGet();
        Object[] s = getSample(sample, n - 1);
        String sampleStr = "";
        if (s != null && s.length != 0) {
            sampleStr = Arrays.stream(s).map(r -> String.valueOf(r)).collect(Collectors.joining(", ", "[", "]"));
        }
        T t = dataBase.getDataBase();
        TaskCallable<V> callable = new TaskCallable<V>() {
            @Override
            public V run() throws Exception {
                return onStartTest(t, s);
            }
        };
        long time = System.nanoTime();
        Future<V> future = executorService.submit(callable);
        long runTime = 0;
        while (true) {
            runTime = System.nanoTime() - time;
            if (callable.isFinish()) {
                testFinish(future, n, sampleStr, runTime);
                break;
            } else if (runTime > timeout) {
                if (timeoutStop) {
                    isStop = true;
                }
                results.addTimeoutCount(n);
                LoggerUtil.detail(
                        getTestName(),
                        n,
                        (double) n * 100 / totalNum,
                        runTime,
                        sampleStr,
                        "Execute timeout!");
                onTimeout(n);
                future.cancel(true);
                break;
            }
        }
    }

    /**
     * 测试完成后调用该方法打印日志，进行信息记录
     *
     * @param future    执行测试的任务线程
     * @param count     当前测试序号
     * @param sampleStr 样本
     * @param runTime   测试耗时
     */
    private void testFinish(Future<V> future, int count, String sampleStr, long runTime) {
        String result = "Results is null.";
        try {
            V v = future.get();
            if (v != null) {
                result = resultHandle(v);
            }
            results.addTime(runTime);
        } catch (Throwable e) {
            if (errorStop) {
                isStop = true;
            }
            if (results.addErrorCount(count)) {
                onError(count);
            }
            LOG.error(String.format("There was an error executing item %d!", count), e);
            result = "Execute error.";
        }
        LoggerUtil.detail(
                getTestName(),
                count,
                (double) count * 100 / totalNum,
                runTime,
                sampleStr,
                result);
    }

    /**
     * 设置样本
     *
     * @param key
     */
    @Override
    public void setSample(String key) {
        if (key != null && !key.equals("")) {
            sample = TestConfiguration.instance().getSample(key);
        }
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout * 1000000;
    }

    /**
     * 实际进行测试的方法，返回测试结果的证明数据
     *
     * @param s 样本集合
     * @return 测试结果
     */
    public abstract V onStartTest(T dataBase, Object[] s) throws Exception;

    /**
     * 处理测试结果，用于输出测试结果
     *
     * @param result 测试结果
     * @return
     */
    public abstract String resultHandle(V result);

    /**
     * 取得本次执行的样本
     *
     * @param sample 样本集合
     * @param n      当前为第n次执行
     * @return 样本集
     */
    public abstract Object[] getSample(Sample sample, int n);

    @Override
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public void finishTest() {
        executorService.shutdown();
        results.endTest();
        onStopTest();
        LOG.info("-- End " + getTestName());
    }

    /**
     * 取得测试响应体，用于自定义添加data
     *
     * @return
     */
    @Override
    public TestResults getResults() {
        return results;
    }

    /**
     * 测试任务封装
     *
     * @param <V>
     */
    abstract class TaskCallable<V> implements Callable<V> {

        /**
         * 测试是否已经完成
         * 交给主线程进行判断，用于获取当前测试任务的执行状态
         */
        private volatile boolean isFinish;

        public boolean isFinish() {
            return isFinish;
        }

        @Override
        public V call() throws Exception {
            V v = null;
            try {
                v = run();
            } finally {
                isFinish = true;
            }
            return v;
        }

        public abstract V run() throws Exception;
    }

    /**
     * 前置处理器，初始化用，此时样本已经初始化完成，但是results还未初始化
     */
    protected void onInitTest() {

    }

    /**
     * 后置处理，即将关闭当前测试项时执行
     */
    protected void onStopTest() {

    }

    /**
     * 错误处理器，当测试出现错误时调用当前处理器
     *
     * @param count 测试错误的序号
     */
    protected void onError(int count) {

    }

    /**
     * 超时处理器，测试超时时执行
     * 此时超时线程已经被关闭了
     *
     * @param count 测试超时的序号
     */
    protected void onTimeout(int count) {

    }
}
