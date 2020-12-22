package com.galaxybase.benchmark.hugegraph.util;

import com.galaxybase.benchmark.common.util.LoggerUtil;
import com.galaxybase.benchmark.common.util.TestConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author linsongwang
 * @date 2019/12/11
 */
public class HttpConnect {
    public static final String SEND_GET = "GET";

    /**
     * 进行请求发送的方法
     * @param method 请求类型
     * @param url url
     * @param body body 内容
     * @param heads 请求头
     * @return 响应体内容
     */
    private static String send(String method, String url, String body, Map<String, String> heads) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connect = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connect.setRequestProperty("accept", "*/*");
            connect.setRequestProperty("connection", "Keep-Alive");
            connect.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (heads != null) {
                for (Map.Entry<String, String> entry : heads.entrySet()) {
                    connect.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connect.setReadTimeout(TestConfiguration.INSTANCE.getTimeout());
            // 设置请求格式
            connect.setRequestMethod(method);
            // 发送请求body参数
            if (body != null) {
                connect.setDoOutput(true);
                connect.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(connect.getOutputStream());
                out.println(body);
                // flush输出流的缓冲
                out.flush();
            }

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LoggerUtil.LOG.error(String.format("Send %s Request Error!\n"
                    + "- **Request URL：** %s\n- **Request BODY：** %s", method, url, body), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送 GET 请求
     * @param url 请求的 url
     * @param param 请求携带的参数
     * @return 响应体内容
     */
    public static String sendGet(String url, String param) {
        return send(SEND_GET, url + "?" + param, null, null);
    }
}
