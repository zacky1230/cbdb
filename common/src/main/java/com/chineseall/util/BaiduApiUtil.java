package com.chineseall.util;

import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:07.
 */
public class BaiduApiUtil {
    private static final String clientId = "ZwYK2casgWk7x1y3KGqvZIhK";
    private static final String clientSecret = "6BlZhBKEXdS6RyN5EiookGCF96UPo7zk";

    public static String accessToken() {
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=clientId" +
                "&client_secret=clientSecret";
        JSONObject jsonObject = HttpClientUtil.httpGet(url.replace("clientId", clientId).replace("clientSecret",
                clientSecret));
        return jsonObject.get("access_token").toString();
    }

    public static String getImageContext(String filePath,String accessToken) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general";
        String result = null;
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
//            String accessToken = "#####调用鉴权接口获取的token#####";
            result = HttpUtil.post(url, accessToken, params);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
