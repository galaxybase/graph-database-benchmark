package com.galaxybase.benchmark.hugegraph.test.util;

import com.baidu.hugegraph.structure.gremlin.ResultSet;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.benchmark.hugegraph.database.Huge;

/**
 * @author cl32
 * 2020/11/10
 */
public abstract class HugeTest extends AbstractTest<Huge.HugeConnect, ResultSet> {
    protected final String vertexType;
    protected final String edgeType;
    protected final int vertexLabelId;
    protected final boolean prop;

    HugeTest() {
        TestConfiguration conf = TestConfiguration.INSTANCE;
        vertexType = conf.get("vertexType");
        edgeType = conf.get("edgeType");
        prop = conf.getOrDefault("prop", false);
        vertexLabelId = dataBase.getDataBase().getVertexLabelId();
    }

    @Override
    public ResultSet onStartTest(Huge.HugeConnect dataBase, Object[] s) {
        return dataBase.executeGremlin(getGremlin(s));
    }

    /**
     * 取得gremlin命令
     * @param s 样本
     * @return gremlin命令内容
     */
    protected abstract String getGremlin(Object[] s);
}
