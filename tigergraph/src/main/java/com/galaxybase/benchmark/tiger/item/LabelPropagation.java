package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.tiger.abstr.AbstractAlgorithm;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author chenyanglin
 * @Date 2020/12/14 13:55
 * @Version 1.0
 */
public class LabelPropagation extends AbstractAlgorithm {

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[] {10};
    }

    @Override
    public String getTestName() {
        return "LPA";
    }

    @Override
    public String invokeQuery(TigerGraph graph, Object[] s) {
        Map<String, Object> params = new HashMap<>();
        // 最大迭代次数
        params.put("maxIter", s[0]);
        String response = graph.queryRequest("Label_Prop", params);
        System.out.println(response);
        return response;
    }

}