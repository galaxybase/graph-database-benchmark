package com.galaxybase.benchmark.tiger.util;

import com.galaxybase.benchmark.tiger.enums.HttpEnum;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

/**
 * 发送Http请求工具类
 *
 * @Author chenyanglin
 * @Date 2020/11/5 16:57
 * @Version 1.0
 */
public final class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    // 超时时间测试为1小时
    private static int timeout = 3600 * 1000;

    private static CloseableHttpClient httpClient = null;
    private static PoolingHttpClientConnectionManager poolConnManager = null;

    static {
        try {
//            logger.info("初始化HttpClientTest~~~开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 同时最多连接数
            poolConnManager.setMaxTotal(Integer.valueOf("200"));
            // 设置最大路由
            poolConnManager.setDefaultMaxPerRoute(Integer.valueOf("100"));
            // 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
            // 1、MaxtTotal是整个池子的大小；
            // 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
            // MaxtTotal=400 DefaultMaxPerRoute=200
            // 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
            // 而我连接到http://www.bac.com 和
            // http://www.ccd.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute
            // 初始化httpClient
            httpClient = getConnection();

//            logger.info("初始化HttpClientTest~~~结束");
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private HttpClientUtil() {}

    /**
     * 发送http GET请求
     *
     * @param url 请求地址
     *
     * @return response
     */
    public static String httpGet(String url) {
        return http(url, null, HttpEnum.HTTP_GET, null);
    }

    /**
     * 发送http GET请求
     *
     * @param url 请求地址
     * @param params 请求参数
     *
     * @return response
     */
    public static String httpGet(String url, Map params) {
        return http(url, params, HttpEnum.HTTP_GET, null);
    }

    /**
     * 发送http GET请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpGet(String url, Map params, Header... headers) {
        return http(url, params, HttpEnum.HTTP_GET, headers);
    }

    /**
     * 发送http GET请求
     *
     * @param url 请求地址
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpGet(String url, Header... headers) {
        return http(url, null, HttpEnum.HTTP_GET, headers);
    }

    /**
     * 发送http POST请求
     *
     * @param url 请求地址
     *
     * @return response
     */
    public static String httpPost(String url) {
        return httpPost(url, null, null);
    }

    /**
     * 发送http POST请求
     *
     * @param url 请求地址
     * @param params 请求参数
     *
     * @return response
     */
    public static String httpPost(String url, Map params) {
        return httpPost(url, params, null);
    }

    /**
     * 发送http POST请求
     *
     * @param url 请求地址
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpPost(String url, Header... headers) {
        return httpPost(url, null, headers);
    }

//    public static String httpPost(String url, Map requestParams, Header... headers) {
//        String result = null;
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        /** HttpPost */
//        HttpPost httpPost = new HttpPost(url);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry en = it.next();
//            String key = String.valueOf(en.getKey());
//            String value = String.valueOf(en.getValue());
//            if (value != null) {
//                params.add(new BasicNameValuePair(key, value));
//            }
//        }
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            logger.error(String.format("http请求异常，%s", url), e);
//        }
//        if (headers != null) {
//            // 设置请求头
//            httpPost.setHeaders(headers);
//        }
//        /** HttpResponse */
//        CloseableHttpResponse httpResponse = null;
//        try {
//            httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            result = EntityUtils.toString(httpEntity, "utf-8");
//            EntityUtils.consume(httpEntity);
//        } catch (Exception e) {
//            logger.error(String.format("http请求异常，%s", url), e);
//        } finally {
//            try {
//                if (httpResponse != null) {
//                    httpResponse.close();
//                }
//            } catch (IOException e) {
//                logger.info("## release resouce error ##" + e);
//            }
//        }
//        return result;
//    }

    /**
     * 发送http POST请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpPost(String url, Map params, Header... headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        if (params != null) {
            httpPost.setEntity(new StringEntity(params.toString(), "UTF-8"));
        }

        if (headers != null) {
            // 设置请求头
            httpPost.setHeaders(headers);
        }
        CloseableHttpResponse response = null;
        try {
            // 发送请求获取结果
            response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            // 响应状态码
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
                LOGGER.error(String.format("请求%s返回错误码：%s, %s", url, code, result));
                return null;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("http请求异常，%s", url), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送http DELETE请求
     *
     * @param url 请求地址
     *
     * @return response
     */
    public static String httpDelete(String url) {
        return http(url, null, HttpEnum.HTTP_DELETE, null);
    }

    /**
     * 发送http DELETE请求
     *
     * @param url 请求地址
     * @param params 请求参数
     *
     * @return response
     */
    public static String httpDelete(String url, Map params) {
        return http(url, params, HttpEnum.HTTP_DELETE, null);
    }

    /**
     * 发送http DELETE请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpDelete(String url, Map params, Header... headers) {
        return http(url, params, HttpEnum.HTTP_DELETE, headers);
    }

    /**
     * 发送http DELETE请求
     *
     * @param url 请求地址
     * @param headers 请求头
     *
     * @return response
     */
    public static String httpDelete(String url, Header... headers) {
        return http(url, null, HttpEnum.HTTP_DELETE, headers);
    }

    /**
     * 发送http请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param httpType 请求类型
     * @param headers 请求头
     *
     * @return response
     */
    public static String http(String url, Map params, HttpEnum httpType, Header... headers) {
        // 封装带参数的请求地址
        StringBuffer requestUrl = new StringBuffer(url + "?");
        if (params != null) {
            Set<Map.Entry> entries = params.entrySet();
            for (Map.Entry entry : entries) {
                // 请求地址拼接参数
                Object key = entry.getKey();
                String value = entry.getValue() == null ? null : entry.getValue().toString();
                requestUrl.append(key + "=" + URLEncoder.encode(value) + "&");
            }
        }
        // 去掉末尾多余的字符
        requestUrl.deleteCharAt(requestUrl.length() - 1);
        HttpRequestBase httpRequest = getRequest(httpType, requestUrl.toString());

        if (headers != null) {
            // 设置请求头
            httpRequest.setHeaders(headers);
        }
        CloseableHttpResponse response = null;
        try {
            // 发送请求获取结果
            response = httpClient.execute(httpRequest);
            String result = EntityUtils.toString(response.getEntity());
            // 响应状态码
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
//                logger.error(String.format("请求%s返回错误码：%s, %s", url, code, result));
                return null;
            }
        } catch (IOException e) {
//            logger.error(String.format("http请求异常，%s", url), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static HttpRequestBase getRequest(HttpEnum httpType, String requestUrl) {
        System.out.println(requestUrl);
        HttpRequestBase httpRequest = null;
        switch (httpType) {
            case HTTP_GET:
                httpRequest = new HttpGet(requestUrl);
                break;
            case HTTP_PUT:
                httpRequest = new HttpPut(requestUrl);
                break;
            case HTTP_DELETE:
                httpRequest = new HttpDelete(requestUrl);
                break;
            default:
                httpRequest = new HttpGet(requestUrl);
        }
        return httpRequest;
    }

    public static String send(String method, String url, String body, Map<String, String> heads) {
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
            connect.setReadTimeout(Integer.MAX_VALUE);
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
            e.printStackTrace();
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

    public static CloseableHttpClient getConnection() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).build();
        return HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(poolConnManager).setDefaultRequestConfig(config)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, false)).build();
    }

}
