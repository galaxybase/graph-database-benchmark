package com.galaxybase.benchmark.galaxybase.developer.item.abstr;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.driver.Direction;
import com.galaxybase.driver.Graph;
import com.galaxybase.driver.v1.graph.query.bfs.QueryResult;
import com.galaxybase.driver.v1.graph.query.condition.PropertyFilter;
import com.galaxybase.driver.v1.graph.query.condition.PropertyFilterInfo;
import com.galaxybase.driver.v1.graph.query.condition.VisitCondition;
import com.galaxybase.driver.v1.graph.query.condition.VisitConditionByProperty;

import java.math.BigDecimal;
import java.util.*;

public abstract class AbstractBFS extends AbstractTest<Graph, Long> {

    private final String vertexType;
    private final String edgeType;
    private final int depth;
    private List<VisitCondition> conditions;
    private List<Direction> directions;
    private List<Set<String>> eTypes;
    private AvgData vertexCount;

    public AbstractBFS() {
        depth = getDepth();
        TestConfiguration conf = TestConfiguration.INSTANCE;
        vertexType = conf.get("vertexType");
        edgeType = conf.get("edgeType");
        directions = new ArrayList<Direction>();
        directions.add(Direction.OUT);
        if (conf.getOrDefault("prop", false)) {
            //如果配置文件中的参数prop参数为true,则进行对应的边过滤
            PropertyFilter propertyFilter = new PropertyFilter();
            PropertyFilterInfo propertyFilterInfo = new PropertyFilterInfo("creationDate",
                    PropertyFilterInfo.QueryMethod.MoreEqualAndLess, 1293811200000L, 1325347200000L);
            propertyFilter.addFilter(propertyFilterInfo);
            Map<String, PropertyFilter> conditionFilterMap = new HashMap<>();
            conditionFilterMap.put(edgeType, propertyFilter);
            VisitCondition vi = new VisitConditionByProperty(conditionFilterMap, false);
            conditions = new ArrayList<>();
            conditions.add(vi);
            eTypes = new ArrayList<>();
            Set<String> set = new HashSet<>();
            set.add(edgeType);
            eTypes.add(set);
        }
        vertexCount = new AvgData();
        getResults().addData("vertexCount", vertexCount);
    }

    /**
     * 执行bfsMaster
     *
     * @param dataBase 图
     * @param s        样本文件
     * @return 返回KHop的点的个数
     */
    public Long onStartTest(Graph dataBase, Object[] s) {
        QueryResult result = dataBase.bfsMaster(String.valueOf(s[0]), vertexType, depth, -1, -1,
                directions, null, conditions, eTypes, isHop(), true, false, false);
        return result.getVertexCount();
    }

    /**
     * 取得当前度数
     *
     * @return
     */
    protected abstract int getDepth();

    protected abstract boolean isHop();

    /**
     * 处理测试结果，用于输出测试结果
     *
     * @param result 测试结果
     * @return 点数量
     */

    public String resultHandle(Long result) {
        vertexCount.addData(BigDecimal.valueOf(result));
        return "点数量：" + result;
    }

    /**
     * 获取样本
     *
     * @param sample 样本文件
     * @return 返回样本中的第n个
     */

    public Object[] getSample(Sample sample, int n) {
        return new Object[]{sample.get(n)};
    }

    /**
     * 打印测试名称
     *
     * @return 返回测试的名称
     */

    public String getTestName() {
        if (isHop()) {
            return "KHop " + getDepth() + "度";
        }
        return "KNeighbor " + getDepth() + "度";
    }
}
