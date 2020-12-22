package com.galaxybase.benchmark.common.util;

import java.util.List;

/**
 * 统计相关的工具类
 */
public class StatisticsUtil {

    /**
     * 计算分位值，N-1法
     *
     * @param n    分位数，大于0，小于1
     * @param list 待求分位值的列表，需要先排序好
     * @return 分位值
     */
    public static double quantileNSub(double n, List<Long> list) {
        if (n <= 0 || n >= 1) {
            throw new RuntimeException("分位值 n 错误：" + n);
        }
        double position = (list.size() - 1) * n + 1;
        int m = (int) position;
        if (m <= 0 || list.size() == 1) {
            return list.get(0);
        }
        long a = list.get(m);
        long b = list.get(m - 1);
        return b + (position - m) * (a - b);
    }
}
