package com.galaxybase.benchmark.galaxybase.developer.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.driver.Graph;
import com.galaxybase.driver.v1.Record;

import java.util.List;

public class DegreeCentrality extends AbstractTest<Graph, List<Record>> {
    @Override
    public List<Record> onStartTest(Graph dataBase, Object[] s) {
        String cypher = getCypher(s);
        return dataBase.executeQuery(cypher).list();
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[0];
    }

    /**
     * 取得 ShortestPath 执行 Cypher
     * @param s
     * @return
     */
    public String getCypher(Object[] s) {
        return "Call DegreeCentrality('OUT', -1)";
    }

    @Override
    public String resultHandle(List<Record> result) {
        return "结果数：" + result.size();
    }

    @Override
    public String getTestName() {
        return "DegreeCentrality";
    }
}
