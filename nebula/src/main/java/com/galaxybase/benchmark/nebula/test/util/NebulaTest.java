package com.galaxybase.benchmark.nebula.test.util;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.vesoft.nebula.client.graph.GraphClient;
import com.vesoft.nebula.client.graph.ResultSet;

/**
 * @author cl32
 * 2020/11/5
 */
public abstract class NebulaTest extends AbstractTest<GraphClient, ResultSet> {
    protected String vertexType;
    protected String edgeType;

    public NebulaTest() {
        TestConfiguration conf = TestConfiguration.INSTANCE;
        vertexType = conf.get("vertexType");
        edgeType = conf.get("edgeType");
    }

    @Override
    public final ResultSet onStartTest(GraphClient dataBase, Object[] s) throws Exception {
        return dataBase.executeQuery(getNsql(s));
    }

    /**
     * 子类传入nsql用
     *
     * @return
     */
    public abstract String getNsql(Object[] s);
}
