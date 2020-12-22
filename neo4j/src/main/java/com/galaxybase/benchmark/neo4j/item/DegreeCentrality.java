package com.galaxybase.benchmark.neo4j.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.neo4j.database.Neo4j;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class DegreeCentrality extends AbstractTest<Session, Record> {
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("CALL algo.degree.stream('" + Neo4j.getVertexType() + "', '"
                + Neo4j.getEdgeType() + "', {weightProperty: null, concurrency:4}) "
                + "YIELD score ");
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
        return "DegreeCentrality";
    }
}
