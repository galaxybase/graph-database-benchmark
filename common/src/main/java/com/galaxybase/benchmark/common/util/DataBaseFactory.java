package com.galaxybase.benchmark.common.util;

import com.galaxybase.benchmark.common.database.DataBase;

public class DataBaseFactory {
    private static DataBase dataBase;

    /**
     * 初始化数据库连接
     *
     * @param clazz
     */
    public static void initDataBase(String clazz) {
        try {
            initDataBase(Class.forName(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库连接
     *
     * @param clazz
     */
    public static void initDataBase(Class clazz) {
        try {
            dataBase = (DataBase) clazz.newInstance();
            dataBase.initDataBase(TestConfiguration.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得数据库连接
     *
     * @return
     */
    public static DataBase getDataBase() {
        if (dataBase == null) {
            initDataBase(TestConfiguration.INSTANCE.getDataBaseClass());
        }
        return dataBase;
    }

    /**
     * 断开数据库连接
     */
    public static void close() {
        if (dataBase != null) {
            dataBase.close();
        }
        dataBase = null;
    }
}
