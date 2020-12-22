package com.galaxybase.benchmark.tiger.abstr;

import com.alibaba.fastjson.JSONObject;
import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.result.data.ResultData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author chenyanglin
 * @Date 2020/12/17 11:35
 * @Version 1.0
 */
public abstract class AbstractBFSMaster extends AbstractTest<TigerGraph, Long> {

    private ResultData resultData;

    public AbstractBFSMaster() {
        resultData = new AvgData();
        getResults().addData((isHop() ? "KHop-" : "KNeighbor-") + getDepth(), resultData);
    }

    @Override
    public Long onStartTest(TigerGraph graph, Object[] s) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("S", s[0]);
        params.put("depth", s[1]);
        String response = graph.queryRequest(isHop() ? "kHop" : "kNeighbor", params);
        return JSONObject.parseObject(response).getJSONArray("results").getJSONObject(0).getLong("vertexCount");
    }

    @Override
    public String resultHandle(Long result) {
        resultData.addData(new BigDecimal(result));
        return "VertexCount:" + result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[] {sample.get(n), getDepth()};
    }

    @Override
    public String getTestName() {
        return (isHop() ? "KHop - " : "KNeighbor - ") + getDepth();
    }

    public abstract int getDepth();

    public abstract boolean isHop();

}
