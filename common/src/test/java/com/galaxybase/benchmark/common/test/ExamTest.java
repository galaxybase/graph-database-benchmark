package com.galaxybase.benchmark.common.test;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.util.Sample;

import java.math.BigDecimal;

/**
 * @author cl32
 * 2020/11/4
 */
public class ExamTest extends AbstractTest<String, Integer> {
    private AvgData data;

    public ExamTest(){
        data = new AvgData();
        getResults().addData("data", data);
    }

    /**
     * 执行测试操作
     * @param dataBase 取得的数据库连接
     * @param s 样本集合
     * @return 数据库取来，还未处理成可以输出的测试结果
     */
    @Override
    public Integer onStartTest(String dataBase, Object[] s) throws Exception {
        int n = 0;
        Thread.sleep((long) (Math.random() * 2000));
        return (Integer) s[0];
    }

    /**
     * 处理测试结果
     * @param result 测试结果
     * @return 可以输出的字段
     */
    @Override
    public String resultHandle(Integer result) {
        data.addData(BigDecimal.valueOf(result));
        return "测试结果为" + result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[]{Integer.parseInt(sample.get(n))};
    }

    @Override
    public String getTestName() {
        return "测试示例";
    }
}
