package com.galaxybase.benchmark.tiger.abstr;


import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

/**
 * Tiger算法测试类
 *
 * @Author chenyanglin
 * @Date 2020/11/19 16:07
 * @Version 1.0
 */
public abstract class AbstractAlgorithm extends AbstractTest<TigerGraph, String> {

    @Override
    public String onStartTest(TigerGraph tigerGraph, Object[] s) throws Exception {
        return invokeQuery(tigerGraph, s);
    }

    @Override
    public String resultHandle(String result) {
        return result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[] {};
    }

    /**
     * 执行算法查询
     * @param tigerGraph
     * @param s
     * @return
     */
    public abstract String invokeQuery(TigerGraph tigerGraph, Object[] s);

}
