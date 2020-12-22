package com.galaxybase.benchmark.common.database;

import com.galaxybase.benchmark.common.util.TestConfiguration;

public interface DataBase<T> {
    /**
     * 取得数据库连接
     */
    T getDataBase();

    /**
     * 初始化数据库连接
     * @param conf
     */
    void initDataBase(TestConfiguration conf);

    /**
     * 关闭数据库连接
     */
    void close();
}
