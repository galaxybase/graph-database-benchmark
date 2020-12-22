package com.galaxybase.benchmark.neo4j.item;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortestPath extends AbstractTest<Session, Record> {
    /**
     * 最短路径算法
     * @param dataBase
     * @param s 样本集合
     * @return
     */
    public Record onStartTest(Session dataBase, Object[] s) {
        StatementResult ans = dataBase.run("match p = shortestpath((v1)-[*..5]->(v2)) where id(v1)=" + s[0]
                + " and id(v2)=" + s[1] + " return p");
        if (ans.hasNext()) {
            return ans.next();
        }
        return null;
    }

    public String resultHandle(Record result) {
        if (result.containsKey("p")) {
            return result.get("p").toString();
        }
        return null;
    }

    public Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }

    public String getTestName() {
        return "ShortestPath";
    }
}
