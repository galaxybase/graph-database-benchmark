package com.galaxybase.benchmark.neo4j.database;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import static com.galaxybase.benchmark.common.util.LoggerUtil.LOG;

public class Neo4j implements DataBase<Session> {

    private static Driver driver;
    private static Session session;
    private static String edgeType;
    private static String vertexType;

    public static String getEdgeType() {
        return edgeType;
    }

    public static void setEdgeType(String edgeType) {
        Neo4j.edgeType = edgeType;
    }

    public static String getVertexType() {
        return vertexType;
    }

    public static void setVertexType(String vertexType) {
        Neo4j.vertexType = vertexType;
    }

    public Session getDataBase() {
        if (session == null) {
            initDataBase(TestConfiguration.INSTANCE);
        }
        return session;
    }

    /**
     * 初始化数据库
     * @param conf
     */
    public void initDataBase(TestConfiguration conf) {
        try {
            // 获取点边类型名称
            edgeType = conf.getOrDefault("edgeType", "person");
            vertexType = conf.getOrDefault("vertexType", "know");
            // 获取数据库连接
            driver = GraphDatabase.driver("bolt://" + conf.getOrDefault("ip", "localhost") + ":"
                            + String.valueOf(conf.getOrDefault("port", 7687)),
                    AuthTokens.basic(String.valueOf(conf.getOrDefault("username", "neo4j")),
                            String.valueOf(conf.getOrDefault("password", "neo4j"))));
            session = driver.session();
        } catch (Exception e) {
            LOG.error("创建neo4j数据库连接失败！", e);
        }
    }

    /**
     * 关闭数据库连接
     */
    public void close() {

        try {
            driver.close();
            session.close();
        } catch (Exception e) {
            LOG.error("neo4j数据库连接关闭失败！", e);
        }
    }
}
