package com.galaxybase.benchmark.neo4j.util;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    private static List<String> vertexes = new ArrayList<>();

    public static List<String> getVertexes() {
        return vertexes;
    }

    public static void setVertexes(List<String> vertexes) {
        DataUtil.vertexes = vertexes;
    }
}
