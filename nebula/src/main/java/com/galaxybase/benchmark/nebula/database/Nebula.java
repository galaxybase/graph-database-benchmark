package com.galaxybase.benchmark.nebula.database;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.facebook.thrift.TException;
import com.google.common.net.HostAndPort;
import com.vesoft.nebula.client.graph.GraphClient;
import com.vesoft.nebula.client.graph.GraphClientImpl;

import java.util.ArrayList;

import static com.galaxybase.benchmark.common.util.LoggerUtil.LOG;

/**
 * @author cl32
 * 2020/11/5
 */
public class Nebula implements DataBase<GraphClient> {

    private static GraphClient client;

    @Override
    public GraphClient getDataBase() {
        if (client == null) {
            initDataBase(TestConfiguration.INSTANCE);
        }
        return client;
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        ArrayList<HostAndPort> list = new ArrayList<HostAndPort>();
        list.add(HostAndPort.fromParts(conf.get("ip"), (int) conf.get("port")));
        client = new GraphClientImpl(list, (int) conf.getTimeout(), 1, 1);
        client.setUser(conf.get("user"));
        client.setPassword(conf.get("passwrod"));
        try {
            client.connect();
            client.switchSpace(conf.getGraphName());
        } catch (TException e) {
            LOG.error("Nebula database connection failed to create!", e);
        }
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            LOG.error("The Nebula database connection failed to close!", e);
        }
    }
}
