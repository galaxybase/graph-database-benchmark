package com.galaxybase.benchmark.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 样本参数管理器
 */
public class SampleManager {
    private Map<String, Sample> samples = new HashMap<>();
    private final Sample defaultSample;

    public SampleManager(String homePath, String defaultPath, Map<String, String> s) {
        defaultSample = buildSample(homePath + defaultPath);
        if (s == null) {
            return;
        }
        for (Map.Entry<String, String> entry : s.entrySet()) {
            String path = homePath + entry.getValue();
            samples.put(entry.getKey(), buildSample(path));
        }
    }

    /**
     * 生成样本对象
     *
     * @param path 样本路径
     * @return 返回样本对象
     */
    public Sample buildSample(String path) {
        return new Sample(path);
    }

    /**
     * 取得默认的样本文件对象
     *
     * @return
     */
    public Sample getDefaultSample() {
        return defaultSample;
    }

    /**
     * 根据key取得样本文件对象
     *
     * @param key
     * @return
     */
    public Sample getSample(String key) {
        return samples.getOrDefault(key, defaultSample);
    }
}
