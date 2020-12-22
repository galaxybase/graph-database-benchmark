package com.galaxybase.benchmark.hugegraph.test;

import com.baidu.hugegraph.structure.gremlin.ResultSet;
import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.hugegraph.test.util.SingleSampleTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cl32
 * 2020/11/10
 */
public abstract class SingleRowTest extends SingleSampleTest {
    private AvgData avgCount;

    public SingleRowTest() {
        avgCount = new AvgData();
        getResults().addData("ResultCount", avgCount);
    }

    @Override
    public final String resultHandle(ResultSet result) {
        List<Object> list = result.data();
        int count = (list == null || list.isEmpty()) ? 0 : (int) list.get(0);
        avgCount.addData(BigDecimal.valueOf(count));
        return "ResultCount " + count;
    }
}
