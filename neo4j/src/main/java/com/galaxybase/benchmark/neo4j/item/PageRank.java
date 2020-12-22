package com.galaxybase.benchmark.neo4j.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.neo4j.database.Neo4j;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class PageRank extends AbstractTest<Session, Record> {
    /**
     * PageRank算法
     * @param dataBase
     * @param s 样本集合
     * @return
     */
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("CALL algo.pageRank('" + Neo4j.getVertexType() + "', '"
                + Neo4j.getEdgeType() + "', "
                + "{iterations:10, concurrency:4})"
                + "YIELD nodes");
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
        return "PageRank";
    }
}

