package com.galaxybase.benchmark.hugegraph.test;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.hugegraph.database.Huge;

import java.math.BigDecimal;

/**
 * @author cl32
 * 2020/11/10
 */
public class ShortestPathTest extends AbstractTest<Huge.HugeConnect, Integer> {
    private AvgData avgPathLength;

    public ShortestPathTest() {
        avgPathLength = new AvgData();
        getResults().addData("PathLength", avgPathLength);
    }

    @Override
    public Integer onStartTest(Huge.HugeConnect dataBase, Object[] s) {
        return dataBase.shortestPath((String) s[0], (String) s[1]);
    }

    @Override
    public String resultHandle(Integer result) {
        avgPathLength.addData(BigDecimal.valueOf(result));
        return "Path Length " + result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }

    @Override
    public String getTestName() {
        return "ShortestPathTest";
    }
}
