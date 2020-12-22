package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.tiger.abstr.AbstractAlgorithm;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author chenyanglin
 * @Date 2020/12/14 13:57
 * @Version 1.0
 */
public class PageRank extends AbstractAlgorithm {

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[] {0, 10, 0.85, true, 1};
    }

    @Override
    public String getTestName() {
        return "PageRank";
    }

    @Override
    public String invokeQuery(TigerGraph graph, Object[] s) {
        Map<String, Object> params = new HashMap<>();
        params.put("maxChange", s[0]);
        params.put("maxIter", s[1]);
        params.put("damping", s[2]);
        params.put("display", s[3]);
        params.put("outputLimit", s[4]);
        return graph.queryRequest("PageRank", params);
    }

}