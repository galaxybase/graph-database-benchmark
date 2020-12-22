package com.galaxybase.benchmark.common.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sample {
    private List<String> sample;
    private int size;

    public Sample(String path) {
        load(path);
    }

    /**
     * 取得指定行样本
     *
     * @param num 取得指定行样本，如果超出数组长度则取余
     * @return
     */
    public String get(int num) {
        return sample.get(num % size);
    }

    /**
     * 加载样本文件
     *
     * @param path 样本文件路径
     */
    public void load(String path) {
        BufferedReader reader = null;
        sample = new ArrayList<String>();
        int i = 1;
        try {
            reader = new BufferedReader(new FileReader(path));
            String str = null;
            while ((str = reader.readLine()) != null) {
                sample.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        size = sample.size();
    }

    /**
     * 取得整个样本集
     *
     * @return
     */
    public List<String> samples() {
        return sample;
    }

    /**
     * 根据主键取得姓名
     * @param key 主键
     * @return 姓名
     */
    public static String getIdName(long key) {
        String[] names = {"张", "李", "王", "赵", "钱", "刘", "秦", "张", "郑", "林", "吴"};
        String[] names2 = {"三", "四", "五", "六", "七", "安", "乐", "则", "钉", "佳", "勇", "傲"};
        String[] names3 = {"腌", "带", "范", "空", "噢", "安", "乐", "图", "清", "佳", "囸", "琪", "可"};
        return names[(int) key % names.length] + names2[(int) key % names2.length] + names3[(int) key % names3.length];
    }

    /**
     * 根据主键取得男女
     * @param key 主键
     * @return 性别
     */
    public static String getIdSex(long key) {
        return (key % 2 == 1 ? "男" : "女");
    }

    /**
     * 取得随机int数据，小于num
     * @param num 随机数最大值
     * @return 随机数
     */
    public static int randomInt(int num) {
        return (int) (Math.random() * num);
    }

    /**
     * 根据主键取得年龄
     * @param key 主键
     * @return 年龄
     */
    public static int getIdAge(long key) {
        return (int) key % 60;
    }

    /**
     * 根据主键取得国家
     * @param key 主键
     * @return 国家
     */
    public static String getIdCountry(long key) {
        String[] country = {"印度", "西班牙", "蒙古", "越南", "中国", "美国", "英国", "法国", "日本", "德国", "俄罗斯", "澳大利亚", "加拿大", "刚果"};
        return country[(int) (key % country.length)];
    }

    /**
     * 取得随机科目
     * @param key 主键
     * @return 科目
     */
    public static String getIdSubject(long key) {
        String[] country = {"语文", "数学", "英语", "物理", "化学", "生物", "思想品德", "政治", "历史", "地理"};
        return country[(int) key % country.length];
    }
}
