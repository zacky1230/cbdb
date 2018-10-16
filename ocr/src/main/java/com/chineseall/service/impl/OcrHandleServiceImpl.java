package com.chineseall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.FileUtil;
import com.chineseall.util.HttpClientUtil;
import com.chineseall.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:08.
 */
@Service
public class OcrHandleServiceImpl implements OcrHandleService {
    private Logger logger = LoggerFactory.getLogger(OcrHandleServiceImpl.class);

    @Override
    public int[] ocrImageHandle(String realPath, String page) {
        String resultImgPath = picture2Binary(realPath);
        return detectLines(resultImgPath, page);
    }

    private int[] detectLines(String resultImgPath, String page) {
        int[] yIndexes = {};
        String url = "http://127.0.0.1:5000/detectVerticalLine?filePath=FilePath&page=Page";
        JSONObject jsonObject;
        try {
            if (StringUtils.isEmpty(resultImgPath) && StringUtils.isEmpty(page)) {
                return yIndexes;
            }
            jsonObject = HttpClientUtil.httpGet(url.replace("FilePath", resultImgPath).replace("Page", page));
            logger.info(String.format("The y axis of the image is : %s", jsonObject.toJSONString()));
            String data = jsonObject.getString("data");
            String[] datas = data.substring(1, data.length() - 1).split(",");
            yIndexes = new int[datas.length];
            for (int i = 0; i < datas.length; i++) {
                yIndexes[i] = Integer.parseInt(datas[i]);
            }
            return yIndexes;
        } catch (Exception e) {
            return yIndexes;
        }
    }


    private String picture2Binary(String realPath) {
        String url = "http://127.0.0.1:5000/image2Binary?filePath=FilePath&fileName=FileName";
        String filePath = FileUtil.getParentDirectory(realPath);
        String fileName = FileUtil.getFileName(realPath);
        JSONObject jsonObject;
        try {
            if (StringUtils.isEmpty(fileName) && StringUtils.isEmpty(filePath)) {
                return "fail";
            }
            assert filePath != null;
            assert fileName != null;
            jsonObject = HttpClientUtil.httpGet(url.replace("FilePath", filePath).replace("FileName",
                    fileName));
            logger.info(String.format("The image %s binary success, the save info is %s", realPath, jsonObject
                    .toJSONString()));
            return jsonObject.get("imgPath").toString();
        } catch (Exception e) {
            return "fail";
        }

    }

}
