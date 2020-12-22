package com.galaxybase.benchmark.common.util;

import java.text.SimpleDateFormat;

public class StringTool {
    /**
     * 格式化毫秒时间
     *
     * @param time 毫秒时间戳
     * @return 时间
     */
    public static String getTextMsTime(long time) {
        StringBuffer sb = new StringBuffer();
        sb.insert(0, time % 1000 + "ms");
        time = time / 1000;
        if (time <= 0) {
            return sb.toString();
        }
        sb.insert(0, time % 60 + "s ");
        time = time / 60;
        if (time <= 0) {
            return sb.toString();
        }
        sb.insert(0, time % 60 + "m ");
        time = time / 60;
        if (time <= 0) {
            return sb.toString();
        }
        sb.insert(0, time % 60 + "h ");
        return sb.toString();
    }

    /**
     * 格式化纳秒时间
     *
     * @param time 纳秒时间戳
     * @return 时间
     */
    public static String getTextNsTime(long time) {
        StringBuffer sb = new StringBuffer();
        sb.insert(0, time % 1000 + "ns");
        time = time / 1000;
        if (time <= 0) {
            return sb.toString();
        }
        sb.insert(0, time % 1000 + "us ");
        time = time / 1000;
        if (time <= 0) {
            return sb.toString();
        }
        sb.insert(0, getTextMsTime(time) + " ");
        return sb.toString();
    }

    /**
     * 取得纳秒级的详细时间
     *
     * @param time 纳秒时间戳
     * @return 信息时间
     */
    public static String getDetailNsTime(long time) {
        return String.format("%s(%.2fms)", getTextNsTime(time), (double) time / 1000000);
    }

    /**
     * 时间戳格式化
     *
     * @param time   毫秒时间戳
     * @param format 时间格式
     * @return String内容
     */
    public static String timeStampToString(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(time);
    }

    /**
     * 时间戳转图对应的数字时间
     *
     * @param time 毫秒时间戳
     * @return 数字时间
     */
    public static long timeStampToGraphTime(long time) {
        return Long.parseLong(timeStampToString(time, "yyyyMMddHHmmssSSS"));
    }

    /**
     * 取得当前时间，格式为yyyyMMddHHmmss
     *
     * @return
     */
    public static String currentTimeString() {
        return timeStampToString(System.currentTimeMillis(), "yyyyMMddHHmmss");
    }

    /**
     * 取得当前时间
     *
     * @return 当前时间
     */
    public static String presentTime() {
        return timeStampToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 处理测试结果输出的分割符
     *
     * @param text 内容
     * @return 分隔符替换后内容
     */
    public static String splitDispose(String text) {
        return text == null ? "is Null." : text.replace("|", " ");
    }
}
