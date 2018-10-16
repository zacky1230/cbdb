package com.chineseall.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:11.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static RequestConfig requestConfig = null;

    /**
     * 设置请求和传输超时时间
     */
    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(2000 * 300).setConnectTimeout(2000).build();
    }

    /**
     * post请求传输json参数
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            jsonResult = getJsonObject(result, url);
        } catch (IOException e) {
            logger.error(String.format("Url %s post请求提交失败: %s" + url, e));
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    private static JSONObject getJsonObject(CloseableHttpResponse result, String url) {
        JSONObject jsonResult = null;
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str;
            try {
                str = EntityUtils.toString(result.getEntity(), "utf-8");
                jsonResult = JSONObject.parseObject(str);
            } catch (Exception e) {
                logger.error(String.format("Url %s post请求提交失败: %s" + url, e));
            }
        }
        return jsonResult;
    }

    /**
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     *
     * @param url      url地址
     * @param strParam 参数
     * @return
     */
    public static JSONObject httpPost(String url, String strParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            jsonResult = getJsonObject(result, url);
        } catch (IOException e) {
            logger.error(String.format("Url %s post请求提交失败: %s" + url, e));
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static JSONObject httpGet(String url) {
        JSONObject jsonResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                jsonResult = JSONObject.parseObject(strResult);
            } else {
                logger.error(String.format("Url %s get请求提交失败: %s" + url));
            }
        } catch (IOException e) {
            logger.error(String.format("Url %s get请求提交失败: %s" + url, e));
        } finally {
            request.releaseConnection();
        }
        return jsonResult;
    }

    public static void main(String[] args) {
        String realPath = "/Users/zacky/Desktop/PicHandle/original.tif";
        String url = "http://127.0.0.1:5000/image2Binary?filePath=FilePath&fileName=FileName";
        String filePath = FileUtil.getParentDirectory(realPath);
        String fileName = FileUtil.getFileName(realPath);
        JSONObject jsonObject = HttpClientUtil.httpGet(url.replace("FilePath", filePath).replace("FileName",
                fileName));
        System.out.println(jsonObject);
    }
}
