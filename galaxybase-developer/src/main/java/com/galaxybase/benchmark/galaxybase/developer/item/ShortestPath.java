package com.galaxybase.benchmark.galaxybase.developer.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.driver.Graph;
import com.galaxybase.driver.v1.Record;

import java.util.List;

public class ShortestPath extends AbstractTest<Graph, List<Record>> {
    protected final String vertexType;
    protected final String edgeType;

    public ShortestPath() {
        TestConfiguration conf = TestConfiguration.INSTANCE;
        vertexType = conf.get("vertexType");
        edgeType = conf.get("edgeType");
    }

    @Override
    public List<Record> onStartTest(Graph dataBase, Object[] s) {
        String cypher = getCypher(dataBase, s);
        return dataBase.executeQuery(cypher).list();
    }


    @Override
    public Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }

    /**
     * 取得执行 Cypher
     * @param dataBase 数据库连接
     * @param s 参数
     * @return Cypher 命令
     */
    public String getCypher(Graph dataBase, Object[] s) {
        long vertex1Id = dataBase.getElementId(String.valueOf(s[0]), vertexType);
        long vertex2Id = dataBase.getElementId(String.valueOf(s[1]), vertexType);
        return String.format("Call ShortestPath(%s,%s,'OUT',5)", vertex1Id, vertex2Id);
    }

    /**
     * 处理测试结果，用于输出测试结果
     *
     * @param result 测试结果
     * @return 返回路径的长度
     */
    @Override
    public String resultHandle(List<Record> result) {
        return "路径长度 = " + (result.size() - 1);
    }

    /**
     * 打印测试名称
     *
     * @return 返回测试的名称
     */
    @Override
    public String getTestName() {
        return "ShortestPath";
    }
}
