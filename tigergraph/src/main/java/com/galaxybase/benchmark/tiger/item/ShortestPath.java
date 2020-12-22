package com.galaxybase.benchmark.tiger.item;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * Tiger最短路径测试类
 *
 * @Author chenyanglin
 * @Date 2020/11/19 16:07
 * @Version 1.0
 */
public class ShortestPath extends AbstractTest<TigerGraph, String> {

    @Override
    public String onStartTest(TigerGraph graph, Object[] s) {
        Map<String, Object> params = new HashMap<>();
        params.put("S", s[0]);
        params.put("T", s[1]);
        params.put("maxDepth", s[2]);
        String response = graph.queryRequest("Shortest_OUT", params);
        JSONArray StartSet = JSONObject.parseObject(response).getJSONArray("results").getJSONObject(0)
                .getJSONArray("StartSet");
        return JSONObject.parseObject(response).getJSONArray("results").getJSONObject(0)
                .getJSONArray("StartSet").getJSONObject(0).getJSONObject("attributes").getString("length");
    }

    @Override
    public String resultHandle(String result) {
        return result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        String[] ids = sample.get(n).split(",");
        return new Object[] {ids[0], ids[1], 5};
    }

    @Override
    public String getTestName() {
        return "ShortestPath";
    }

}
