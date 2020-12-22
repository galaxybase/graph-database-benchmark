package com.galaxybase.benchmark.common.util;

import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoggerUtil {
    public static final Logger LOG = Logger.getLogger("sys");
    public static final Logger DETAIL_CSV = Logger.getLogger("detail_csv");
    public static final Logger STATISTICS_CSV = Logger.getLogger("statistics_csv");
    private static final String STATISTICS_FILE = "%s/%s-%s.log";
    private static final String STATISTICS_CSV_FILE = "%s/%s-%s.csv";
    private static final String DETAIL_FILE = "%s/%s-%s-detail.log";
    private static final String DETAIL_CSV_FILE = "%s/%s-%s-detail.csv";
    private static final String MOVE_PATH = "%s/%s-%s_back-%s";
    private static final String DETAIL_FORMAT = "\t%d\tSchedule: %.2f%%\tTime: %s\tSample: %s\tResult %s";
    private static final String DETAIL_CSV_FORMAT = "%s|%s|%d|%d|%s|%s";

    /**
     * 通过配置类进行日志初始化
     *
     * @param conf 测试配置
     */
    public static void initLog(TestConfiguration conf) {
        String logPath = conf.getHomePath();
        String graphType = conf.getGraphType();
        String graphName = conf.getGraphName();
        onInit(logPath, graphType, graphName);
    }

    /**
     * 指定日志参数进行日志初始化
     *
     * @param logPath   日志所在路径
     * @param graphType 图数据库名称
     * @param graphName 图名称
     */
    public static void initLog(String logPath, String graphType, String graphName) {
        onInit(logPath, graphType, graphName);
    }

    /**
     * 指定日志参数进行日志初始化
     *
     * @param logPath   日志所在路径
     * @param graphType 图数据库名称
     * @param graphName 图名称
     */
    private static void onInit(String logPath, String graphType, String graphName) {

        File moveFile = new File(String.format(MOVE_PATH, logPath, graphType,
                graphName, StringTool.currentTimeString()));
        fileMove(logPath, moveFile, STATISTICS_FILE, graphType, graphName);
        fileMove(logPath, moveFile, DETAIL_FILE, graphType, graphName);
        boolean statistics = fileMove(logPath, moveFile, STATISTICS_CSV_FILE, graphType, graphName);
        boolean detail = fileMove(logPath, moveFile, DETAIL_CSV_FILE, graphType, graphName);

        LOG.setAdditivity(false);
        LOG.removeAllAppenders();
        LOG.setLevel(Level.DEBUG);
        // 详细文件
        String conversion = "[%-d{yyyy-MM-dd HH:mm:ss}] [%p]\t%m%n";
        PatternLayout layout = new PatternLayout(conversion);
        RollingFileAppender fileAppender = new RollingFileAppender();
        fileAppender.setName("fileAppender");
        fileAppender.setLayout(layout);
        String path = String.format(DETAIL_FILE, logPath, graphType, graphName);
        fileAppender.setFile(path);
        fileAppender.setThreshold(Level.DEBUG);
        fileAppender.setEncoding("UTF-8");
        fileAppender.activateOptions();
        LOG.addAppender(fileAppender);

        // 统计文件
        fileAppender = new RollingFileAppender();
        fileAppender.setName("fileAppenderLog");
        fileAppender.setLayout(layout);
        path = String.format(STATISTICS_FILE, logPath, graphType, graphName);
        fileAppender.setFile(path);
        fileAppender.setThreshold(Level.INFO);
        fileAppender.setEncoding("UTF-8");
        fileAppender.activateOptions();
        LOG.addAppender(fileAppender);

        // 控制台
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setName("consoleAppender");
        consoleAppender.setThreshold(Level.DEBUG);
        consoleAppender.setLayout(layout);
        consoleAppender.setTarget("System.out");
        consoleAppender.setEncoding("UTF-8");
        consoleAppender.setImmediateFlush(true);
        consoleAppender.activateOptions();
        LOG.addAppender(consoleAppender);
        onInitCsvLog(statistics, detail, logPath, graphType, graphName);
    }

    /**
     * 初始化csv日志记录器
     *
     * @param logPath
     * @param graphType
     * @param graphName
     */
    private static void onInitCsvLog(boolean statistics, boolean detail,
                                     String logPath, String graphType, String graphName) {
        STATISTICS_CSV.setAdditivity(false);
        STATISTICS_CSV.removeAllAppenders();
        STATISTICS_CSV.setLevel(Level.DEBUG);
        String conversion = "%m%n";
        PatternLayout layout = new PatternLayout(conversion);
        // 统计文件
        RollingFileAppender fileAppender = new RollingFileAppender();
        fileAppender.setName("csvFileAppenderLog");
        fileAppender.setLayout(layout);
        String path = String.format(STATISTICS_CSV_FILE, logPath, graphType, graphName);
        fileAppender.setFile(path);
        fileAppender.setThreshold(Level.INFO);
        fileAppender.setEncoding("UTF-8");
        fileAppender.activateOptions();
        STATISTICS_CSV.addAppender(fileAppender);
        if (!statistics) {
            STATISTICS_CSV.info("test_time|operation_type|min_time|max_time|avg_time|P99|P999|"
                    + "throughput|success_num|success_rate|error_num|error_rate|timeout_num|timeout_rate|data");
        }
        DETAIL_CSV.setAdditivity(false);
        DETAIL_CSV.removeAllAppenders();
        DETAIL_CSV.setLevel(Level.DEBUG);
        // 详细文件
        fileAppender = new RollingFileAppender();
        fileAppender.setName("csvFileAppender");
        fileAppender.setLayout(layout);
        path = String.format(DETAIL_CSV_FILE, logPath, graphType, graphName);
        fileAppender.setFile(path);
        fileAppender.setThreshold(Level.DEBUG);
        fileAppender.setEncoding("UTF-8");
        fileAppender.activateOptions();
        DETAIL_CSV.addAppender(fileAppender);
        if (!detail) {
            DETAIL_CSV.debug("test_time|operation_type|serial|run_time|samples|results");
        }
    }

    private static boolean fileMove(String path, File moveFile, String format, String graphType, String graphName) {
        File file = new File(String.format(format, path, graphType, graphName));
        if (file.exists()) {
            if (!moveFile.exists()) {
                moveFile.mkdirs();
            }
            File refile = new File(String.format(format, moveFile.getAbsolutePath(), graphType, graphName));
            try {
                Files.copy(file.toPath(), refile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 输出详细测试信息
     *
     * @param name      测试项名称
     * @param count     测试序号
     * @param rate      进度
     * @param runTime   执行时间
     * @param sampleStr 样本集
     * @param result    结果集
     */
    public static void detail(String name, int count, double rate, long runTime, String sampleStr, String result) {
        LOG.debug(String.format(DETAIL_FORMAT, count, rate, StringTool.getTextNsTime(runTime), sampleStr, result));
        DETAIL_CSV.debug(String.format(DETAIL_CSV_FORMAT,
                StringTool.presentTime(),
                StringTool.splitDispose(name),
                count,
                runTime / 1000000,
                StringTool.splitDispose(sampleStr),
                StringTool.splitDispose(result)));
    }
}
