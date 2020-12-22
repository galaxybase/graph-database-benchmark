package com.galaxybase.benchmark.common.test;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;

/**
 * @author cl32
 * 2020/11/4
 */
public class ExamDataBase implements DataBase<String> {
    @Override
    public String getDataBase() {
        return "数据库连接在这里实现！";
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        System.out.println("初始化了数据库连接");
    }

    @Override
    public void close() {
        System.out.println("数据库连接被关闭了");
    }
}
