package com.galaxybase.benchmark.nebula.test;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.benchmark.nebula.test.util.SingleSampleTest;
import com.vesoft.nebula.client.graph.ResultSet;
import com.vesoft.nebula.graph.RowValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cl32
 * 2020/11/6
 * k-hop neighbor 测试
 */
public abstract class KNeighborTest extends SingleSampleTest {
    private final String nsql;
    private final AvgData avgCount;

    public KNeighborTest() {
        avgCount = new AvgData();
        getResults().addData("ResultCount", avgCount);
        String propFilter = "";
        if (TestConfiguration.INSTANCE.getOrDefault("prop", false)) {
            propFilter = String.format(" and %s.creation_date>1293811200000"
                    + " and %s.creation_date<1325347200000", edgeType, edgeType);
        }
        nsql = "GO 1 TO " + depth() + " STEPS FROM %s OVER " + edgeType
                + " WHERE " + edgeType + "._dst != %s" + propFilter
                + " YIELD " + edgeType + "._dst AS vid | GROUP BY $-.vid YIELD 1 AS id "
                + "| GROUP BY $-.id YIELD COUNT(id)";
    }

    @Override
    public String resultHandle(ResultSet result) {
        List<RowValue> list = result.getRows();
        long num = 0;
        if (list.size() != 0) {
            num = list.get(0).getColumns().get(0).getInteger();
        }
        avgCount.addData(BigDecimal.valueOf(num));
        return "resultCount " + num;
    }


    @Override
    public String getNsql(Object[] s) {
        return String.format(nsql, s[0], s[0]);
    }

    @Override
    public String getTestName() {
        return "KNeighborTest - " + depth() + " Depth";
    }

    /**
     * 取得执行的 k-hop neighbor 度数
     * @return
     */
    public abstract int depth();

    public static final class KNeighbor1 extends KNeighborTest {
        @Override
        public int depth() {
            return 1;
        }
    }

    public static final class KNeighbor2 extends KNeighborTest {
        @Override
        public int depth() {
            return 2;
        }
    }

    public static final class KNeighbor3 extends KNeighborTest {
        @Override
        public int depth() {
            return 3;
        }
    }

    public static final class KNeighbor4 extends KNeighborTest {
        @Override
        public int depth() {
            return 4;
        }
    }

    public static final class KNeighbor5 extends KNeighborTest {
        @Override
        public int depth() {
            return 5;
        }
    }

    public static final class KNeighbor6 extends KNeighborTest {
        @Override
        public int depth() {
            return 6;
        }
    }
}
