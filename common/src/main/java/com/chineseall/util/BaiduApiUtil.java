package com.chineseall.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.net.URLEncoder;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:07.
 */
public class BaiduApiUtil {
    private static final String clientId = "ZHpGY8jpxWTpm2otFfUFt3MZ";
    private static final String clientSecret = "wNX22SqzECzzlDp20vVEmfmBEQbhbPbW";

    static Logger logger = LoggerFactory.getLogger(BaiduApiUtil.class);

    public static String accessToken() {
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=clientId" +
                "&client_secret=clientSecret";
        JSONObject jsonObject = HttpClientUtil.httpGet(url.replace("clientId", clientId).replace("clientSecret",
                clientSecret));
        return jsonObject.get("access_token").toString();
    }

    public static String getImageContext(String filePath, String accessToken) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        String result = null;
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            result = HttpUtil.post(url, accessToken, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getBufferedImageContext(BufferedImage bufferedImage, String format, String accessToken) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=" + accessToken;
        String result = null;
        JSONObject jsonObject;
        try {
            byte[] imgData = FileUtil.imageToBytes(bufferedImage, format);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            result = HttpUtil.post(url, accessToken, params);
            jsonObject = JSONObject.parseObject(result);
            if (!jsonObject.toJSONString().contains("error_msg")) {
                JSONArray words_result = JSONObject.parseArray(jsonObject.get("words_result").toString());
                StringBuffer s = new StringBuffer();
                for (int i = 0; i < words_result.size(); i++) {
                    s.append(JSONObject.parseObject(words_result.get(i).toString()).getString("words") + " ");
                }
                result = s.toString();
                /*result = JSONObject.parseObject(JSONObject.parseArray(jsonObject.get("words_result").toString()).get(0).toString
                        ()).getString("words");*/
            } else {
                result = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String msg = "{\"log_id\": 5271106195237898813, \"words_result_num\": 1, \"words_result\": [{\"words\": " +
                "\"府都會之雄已也在易觀之\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        logger.info(JSONObject.parseObject(JSONObject.parseArray(jsonObject.get("words_result").toString()).get(0).toString
                ()).getString("words"));
    }
}
