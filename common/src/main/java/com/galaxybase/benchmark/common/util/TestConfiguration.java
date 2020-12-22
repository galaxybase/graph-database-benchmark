package com.galaxybase.benchmark.common.util;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试配置管理器
 */
public final class TestConfiguration {
    public static TestConfiguration INSTANCE = null;
    private static String homePath = jarPath();
    private String graphType;
    private String graphName;
    private String dataBaseClass;
    private SampleManager sampleManager;
    private int timeout;
    private boolean errorStop;
    private boolean timeoutStop;

    /**
     * 取得jar所在目录
     */
    public static String jarPath() {
        String jarWholePath = TestConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        return new File(jarWholePath).getParentFile().getAbsolutePath() + File.separator;
    }

    /**
     * 自定义参数列表
     */
    private Map<String, Object> prop;
    /**
     * 测试项列表
     */
    private List<Map<String, Object>> items;

    /**
     * 初始化配置
     *
     * @return
     */
    public static TestConfiguration initConfiguation() {
        if (INSTANCE == null) {
            INSTANCE = initConfiguation(homePath);
        }
        return INSTANCE;
    }

    /**
     * 指定配置文件所在路径，初始化配置
     *
     * @param path 配置文件所在路径
     * @return
     */
    public static TestConfiguration initConfiguation(String path) {
        return initConfiguation(path, "application.yaml");
    }

    /**
     * 指定配置文件所在路径和配置文件名称，初始化配置
     * @param path 配置文件所在路径
     * @param fileName 配置文件名称
     * @return
     */
    public static TestConfiguration initConfiguation(String path, String fileName) {
        homePath = path;
        INSTANCE = new TestConfiguration(path, fileName);
        return INSTANCE;
    }

    /**
     * 通过map的配置信息进行初始化
     * @param prop 配置信息
     * @return
     */
    public static TestConfiguration initConfiguation(Map<String, Object> prop) {
        INSTANCE = new TestConfiguration(prop);
        return INSTANCE;
    }

    /**
     * 取得测试配置项实例
     *
     * @return 配置实例
     */
    public static TestConfiguration instance() {
        if (INSTANCE == null) {
            INSTANCE = initConfiguation();
        }
        return INSTANCE;
    }

    /**
     * 通过yaml初始化
     *
     * @param path yaml文件路径
     */
    private TestConfiguration(String path, String fileName) {
        Yaml yaml = new Yaml();
        InputStream in = null;
        try {
            in = new FileInputStream(path + "/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = yaml.loadAs(in, Map.class);
        initBase(map);
    }

    /**
     * 通过map配置初始化
     */
    private TestConfiguration(Map<String, Object> propMap) {
        if (propMap != null) {
            initBase(propMap);
        }
    }

    /**
     * 初始化基础信息
     *
     * @param map 初始的配置参数
     */
    private void initBase(Map<String, Object> map) {
        graphName = String.valueOf(map.getOrDefault("graphName", "database"));
        graphType = (String) map.getOrDefault("graphType", "graph");
        dataBaseClass = (String) map.getOrDefault("dataBaseClass", "graph");
        timeout = (int) map.getOrDefault("timeout", 3600) * 1000;
        errorStop = (boolean) map.getOrDefault("errorStop", true);
        timeoutStop = (boolean) map.getOrDefault("timeoutStop", true);
        if (map.containsKey("sample")) {
            String path = String.valueOf(map.get("sample"));
            sampleManager = new SampleManager(homePath, path, (Map<String, String>) map.get("samples"));
        }
        if (map.containsKey("test")) {
            items = (List<Map<String, Object>>) map.get("test");
        }
        if (map.containsKey("parameters")) {
            prop = (Map<String, Object>) map.get("parameters");
        }
    }

    public boolean isErrorStop() {
        return errorStop;
    }

    public boolean isTimeoutStop() {
        return timeoutStop;
    }

    public void setErrorStop(boolean errorStop) {
        this.errorStop = errorStop;
    }

    /**
     * 超时时间，单位ms
     *
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getHomePath() {
        return homePath;
    }

    public String getDataBaseClass() {
        return dataBaseClass;
    }

    public void setDataBaseClass(String dataBaseClass) {
        this.dataBaseClass = dataBaseClass;
    }

    /**
     * 取得默认样本
     *
     * @return 默认样本
     */
    public Sample getSample() {
        return sampleManager.getDefaultSample();
    }

    /**
     * 使用key取得样本
     *
     * @param key 样本key
     * @return
     */
    public Sample getSample(String key) {
        return sampleManager.getSample(key);
    }

    public SampleManager getSampleManager() {
        return sampleManager;
    }

    /**
     * 主动设置样本管理器，样本管理器通过path产生
     *
     * @param sampleManager
     */
    public void setSampleManager(SampleManager sampleManager) {
        this.sampleManager = sampleManager;
    }

    /**
     * 添加或者修改自定义参数
     *
     * @param key   自定义参数名称
     * @param value 自定义参数的值
     */
    public void addProp(String key, Object value) {
        if (prop == null) {
            prop = new HashMap<>();
        }
        prop.put(key, value);
    }

    /**
     * 取得自定义参数
     *
     * @param key 参数的key
     * @param <T> 参数值类型
     * @return 参数值
     */
    public <T> T get(String key) {
        if (prop == null) {
            return null;
        }
        return (T) prop.get(key);
    }

    /**
     * 取得自定义参数，如果参数不存在则返回默认值
     * @param key 参数的key
     * @param value 参数的默认值
     * @param <T> 参数蕾西
     * @return
     */
    public <T> T getOrDefault(String key, T value) {
        if (prop == null) {
            return null;
        }
        return (T) prop.getOrDefault(key, value);
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }
}
