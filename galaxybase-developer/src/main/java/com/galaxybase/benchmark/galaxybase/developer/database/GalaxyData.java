package com.galaxybase.benchmark.galaxybase.developer.database;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.driver.GalaxyBase;
import com.galaxybase.driver.Graph;

public class GalaxyData implements DataBase<Graph> {

    private Graph graph;

    @Override
    public Graph getDataBase() {
        if (graph == null) {
            initDataBase(TestConfiguration.INSTANCE);
        }
        return graph;
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        this.graph = GalaxyBase.driver(String.format("bolt://%s:%s", conf.get("ip"), conf.get("port")),
                conf.getGraphName(), (String) conf.get("user"), (String) conf.get("password"));
    }

    @Override
    public void close() {
        try {
            graph.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
