package com.galaxybase.benchmark.tiger.database;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.benchmark.tiger.enums.HttpEnum;
import com.galaxybase.benchmark.tiger.util.HttpClientUtil;

import java.util.Map;

/**
 * TigerGraph图数据库连接类
 *
 * @Author chenyanglin
 * @Date 2020/11/5 14:13
 * @Version 1.0
 */
public class TigerGraph implements DataBase<TigerGraph> {

    private String url;

    public TigerGraph getDataBase() {
        return this;
    }

    public void initDataBase(TestConfiguration conf) {
        url = "http://" + conf.get("ip") + ":" + conf.get("port");
    }

    @Override
    public void close() {

    }

    /**
     * 发送tiger gsql Query请求
     *
     * @param queryName 查询名称
     *
     * @return
     */
    public String queryRequest(String queryName) {
        return HttpClientUtil.httpGet(url + "/query/social/" + queryName);
    }

    /**
     * 发送tiger gsql Query请求
     *
     * @param queryName 查询名称
     *
     * @param params 请求参数
     * @return
     */
    public String queryRequest(String queryName, Map params) {
        return HttpClientUtil.httpGet(url + "/query/social/" + queryName, params);
    }

    /**
     * 调用tiger rest 请求
     *
     * @param restURI 请求URI
     *
     * @param params 请求参数
     * @return
     */
    public String restRequest(String restURI, Map params) {
        return restRequest(restURI, params, HttpEnum.HTTP_GET);
    }

    public String restRequest(String restURI, Map params, HttpEnum httpEnum) {
        if (httpEnum == HttpEnum.HTTP_POST) {
            return HttpClientUtil.httpPost(url + "/graph/social/" + restURI, params);
        } else {
            return HttpClientUtil.http(url + "/graph/social/" + restURI, params, httpEnum);
        }
    }

}
