
package com.dxtf.util;

import com.alibaba.fastjson.JSONObject;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * http请求通用类
 *
 * @author Administrator
 */
public class HttpClientUtil {
    private static final String ENCODING = "UTF-8";
    private static final Log log = LogFactory.getLog(HttpClientUtil.class.getName());
    private static int socketTimeout = 10000;// 连接超时时间，默认10秒
    private static int connectTimeout = 30000;// 传输超时时间，默认30秒
    private static RequestConfig requestConfig;// 请求器的配置
    private static CloseableHttpClient httpClientSSl;// HTTP请求器

    public HttpClientUtil() {
    }

    public static String doRequestGet(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
        builder.setBodyEncoding("UTF-8");
        Set keys;
        Iterator var6;
        String key;
        if (params != null && !params.isEmpty()) {
            keys = params.keySet();
            var6 = keys.iterator();

            while (var6.hasNext()) {
                key = (String) var6.next();
                builder.addQueryParameter(key, (String) params.get(key));
            }
        }

        if (headers != null && !headers.isEmpty()) {
            keys = headers.keySet();
            var6 = keys.iterator();

            while (var6.hasNext()) {
                key = (String) var6.next();
                builder.addHeader(key, (String) headers.get(key));
            }
        }

        Future<Response> f = builder.execute();
        String body = ((Response) f.get()).getResponseBody("UTF-8");
        System.out.println(body);
        http.close();
        return body;
    }

    public static String doRequestPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        log.info("请求地址: " + url);
        log.info("请求参数: " + params);
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding("UTF-8");
        Set keys;
        Iterator var6;
        String key;
        if (params != null && !params.isEmpty()) {
            keys = params.keySet();
            var6 = keys.iterator();

            while (var6.hasNext()) {
                key = (String) var6.next();
                builder.addParameter(key, (String) params.get(key));
            }
        }

        if (headers != null && !headers.isEmpty()) {
            keys = headers.keySet();
            var6 = keys.iterator();

            while (var6.hasNext()) {
                key = (String) var6.next();
                builder.addHeader(key, (String) headers.get(key));
            }
        }

        Future<Response> f = builder.execute();
        String body = ((Response) f.get()).getResponseBody("UTF-8");
        http.close();
        return body;
    }

    public static String doRequestPostStr(String url, String param, Object o) throws IOException, ExecutionException, InterruptedException {
        log.info("请求地址: " + url);
        log.info("请求参数: " + param);
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding("UTF-8");
        builder.setBody(param);
        Future<Response> f = builder.execute();
        String body = ((Response) f.get()).getResponseBody("UTF-8");
        http.close();
        return body;
    }

    public static Map<String, String> doRequestPostJson(String url, String json, Map<String, String> headers) throws Exception {
        System.out.println(json);
        Map<String, String> map = new HashMap();
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.addHeader("Content-type", "application/json; charset=UTF-8");
        String header;
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            Iterator var7 = keys.iterator();

            while (var7.hasNext()) {
                header = (String) var7.next();
                builder.addHeader(header, (String) headers.get(header));
            }
        }

        if (json != null && json != "") {
            builder.setBodyEncoding("UTF-8");
            builder.setBody(json);
        }

        Future<Response> f = builder.execute();
        String body = ((Response) f.get()).getResponseBody("UTF-8");
        header = ((Response) f.get()).getHeader("Location");
        map.put("body", body);
        map.put("header", header);
        http.close();
        System.out.println(map);
        return map;
    }

    public static Map<String, String> doRequestPostJson(String url, JSONObject createParams, Map<String, String> headers) throws Exception {
        Map<String, String> map = new HashMap();
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.addHeader("Content-type", "application/json; charset=UTF-8");
        String header;
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            Iterator var7 = keys.iterator();

            while (var7.hasNext()) {
                header = (String) var7.next();
                builder.addHeader(header, (String) headers.get(header));
            }
        }

        if (createParams != null) {
            String entity = createParams.toString();
            builder.setBodyEncoding("UTF-8");
            builder.setBody(entity);
        }

        Future<Response> f = builder.execute();
        String body = ((Response) f.get()).getResponseBody("UTF-8");
        header = ((Response) f.get()).getHeader("Location");
        map.put("body", body);
        map.put("header", header);
        http.close();
        System.out.println(map);
        return map;
    }

    /**
     * 执行后台Http请求
     *
     * @param params 请求参数
     * @param sslFlg 是否需要ssl加密
     * @return
     */
    public static String invokeRequestWithP12(String url, String params, boolean sslFlg) {
        // 加载证书
        try {
            new HttpClientUtil().initCert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(params, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // 根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        // 设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = null;
            try {
                response = httpClientSSl.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            httpPost.abort();
        }
        return result;
    }

    /**
     * 加载证书
     *
     * @throws Exception
     */
    private void initCert() throws Exception {

        // 证书密码，默认为商户ID
        String key = "1517576291";
        // 证书的路径
        String path = "apiclient_cert.p12";
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
        //Resource resource = new ClassPathResource(path);
        ClassPathResource pathResource = new ClassPathResource(path);
        InputStream inputStream = null;
        try {
            // 指定PKCS12的密码(商户ID)
            inputStream = pathResource.getInputStream();
            keyStore.load(inputStream, key.toCharArray());

        } catch (Exception e) {
            log.error("keyStoreException", e);
        } finally {
            inputStream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
        SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        httpClientSSl = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static String httpGet(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        String result = "";
        try {
            // 连接超时
            httpclient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
            // 读取超时
            httpclient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, 6000);

            HttpGet hg = new HttpGet(url);
            //模拟浏览器
            hg.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
            String charset = "UTF-8";
            hg.setURI(new java.net.URI(url));
            HttpResponse response = httpclient.execute(hg);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                charset = getContentCharSet(entity);
                // 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1
                result = EntityUtils.toString(entity, charset);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 默认编码utf -8
     * Obtains character set of the entity, if known.
     *
     * @param entity must not be null
     * @return the character set, or null if not found
     * @throws ParseException           if header elements cannot be parsed
     * @throws IllegalArgumentException if entity is null
     */
    public static String getContentCharSet(final HttpEntity entity)
            throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }

        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        return charset;
    }
}

