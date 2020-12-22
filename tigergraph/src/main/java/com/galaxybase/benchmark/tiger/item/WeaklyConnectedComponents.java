package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractAlgorithm;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

/**
 * @Author chenyanglin
 * @Date 2020/12/14 13:58
 * @Version 1.0
 */
public class WeaklyConnectedComponents extends AbstractAlgorithm {

    @Override
    public String invokeQuery(TigerGraph graph, Object[] s) {
        return graph.queryRequest("WCC");
    }

    @Override
    public String getTestName() {
        return "WCC";
    }

}