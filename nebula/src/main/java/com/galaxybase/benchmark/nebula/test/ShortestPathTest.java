package com.galaxybase.benchmark.nebula.test;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.nebula.test.util.TwoSampleTest;
import com.vesoft.nebula.client.graph.ResultSet;
import com.vesoft.nebula.graph.RowValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cl32
 * 2020/11/5
 * 最短路径测试
 */
public class ShortestPathTest extends TwoSampleTest {
    private AvgData pathLength = new AvgData();

    public ShortestPathTest() {
        getResults().addData("PathLength", pathLength);
    }

    @Override
    public String resultHandle(ResultSet result) {
        List<RowValue> list = result.getRows();
        int n = Integer.MAX_VALUE;
        for (RowValue r : list) {
            int s = (r.getColumns().get(0).getPath().getEntry_list().size() - 1) / 2;
            n = s < n ? s : n;
        }
        if (n == Integer.MAX_VALUE) {
            n = 0;
        }
        pathLength.addData(BigDecimal.valueOf(n));
        return "Path Length " + n;
    }

    @Override
    public String getNsql(Object[] s) {
        return String.format("FIND SHORTEST PATH FROM %s to %s OVER *  UPTO 5 STEPS;", s[0], s[1]);
    }

    @Override
    public String getTestName() {
        return "ShortestPathTest";
    }
}
