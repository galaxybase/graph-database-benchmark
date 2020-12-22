package com.galaxybase.benchmark.neo4j.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.neo4j.database.Neo4j;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class LabelPropagation extends AbstractTest<Session, Record> {
    /**
     * 标签传播算法
     * @param dataBase
     * @param s 样本集合
     * @return
     */
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("CALL algo.labelPropagation.stream('" + Neo4j.getVertexType()
                + "', '" + Neo4j.getEdgeType() + "', {iterations:10,"
                + "weightProperty:'weight', writeProperty:'partition', "
                + "concurrency:4,direction:'OUTGOING'}) YIELD nodeId, label");
        if (ans.hasNext()) {
            return ans.next();
        }
        return null;
    }

    public String resultHandle(Record result) {
        return null;
    }

    public Object[] getSample(Sample sample, int n) {
        return null;
    }

    public String getTestName() {
        return "LabelPropagation";
    }
}

