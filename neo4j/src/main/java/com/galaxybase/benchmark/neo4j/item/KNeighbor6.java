package com.galaxybase.benchmark.neo4j.item;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class KNeighbor6 extends KNeighbor1 {
    /**
     * 6度k-neoghbor
     * @param dataBase
     * @param s 样本集合
     * @return
     */
    @Override
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("MATCH (a)-[*..6]->(b) WHERE a<>b and id(a)="
                + s[0] + " RETURN COUNT(DISTINCT b) as c");
        if (ans.hasNext()) {
            return ans.next();
        }
        return null;
    }

    @Override
    public String getTestName() {
        return "KNeighbor6";
    }
}