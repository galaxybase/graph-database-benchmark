package com.galaxybase.benchmark.neo4j.item;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.math.BigDecimal;

public class KNeighbor1 extends AbstractTest<Session, Record> {
    private AvgData vertexCount;

    public KNeighbor1() {
        vertexCount = new AvgData();
        getResults().addData("vertexCount", vertexCount);
    }

    /**
     * 1度k-neoghbor
     * @param dataBase
     * @param s 样本集合
     * @return
     */
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("MATCH (a)-[*..1]->(b) WHERE a<>b and id(a)="
                + s[0] + " RETURN COUNT(DISTINCT b) as c");
        if (ans.hasNext()) {
            return ans.next();
        }
        return null;
    }

    public String resultHandle(Record result) {
        if (result.containsKey("c")) {
            vertexCount.addData(BigDecimal.valueOf(result.get("c").asInt()));
            return result.get("c").toString();
        }
        return null;
    }

    public Object[] getSample(Sample sample, int n) {
        return new Object[]{Integer.parseInt(sample.get(n))};
    }

    public String getTestName() {
        return "KNeighbor1";
    }

    @Override
    protected void onTimeout(int count) {
        dataBase.close();
        dataBase.initDataBase(TestConfiguration.instance());
    }
}
