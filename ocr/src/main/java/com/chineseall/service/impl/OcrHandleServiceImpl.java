package com.chineseall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:08.
 */
@Service
public class OcrHandleServiceImpl implements OcrHandleService {
    private Logger logger = LoggerFactory.getLogger(OcrHandleServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public RetMsg ocrImageHandle(Map<String, Object> map) {
        RetMsg retMsg = new RetMsg();
        String realPath = (String) map.get("filePath");
        String page = (String) map.get("page");
        String resultImgPath = picture2Binary(realPath);
        int yAxis[] = detectLines(resultImgPath, page);
        JSONObject json = new JSONObject();
        json.put("yAxis", yAxis);
        json.put("imageId", map.get("imageId"));
        retMsg.setMsg(MessageCode.ImageUploadSuccess.getDescription());
        retMsg.setCode(MessageCode.ImageUploadSuccess.getCode());
        retMsg.setData(json);
        return retMsg;
    }

    @Override
    public RetMsg imageRecognition(Map<String, Object> map) {
        RetMsg retMsg = new RetMsg();
        String imageId = (String) map.get("imageId");
        if (StringUtils.isEmpty(imageId)) {
            retMsg.setMsg(MessageCode.ImageNotFound.getDescription());
            retMsg.setCode(MessageCode.ImageFormatError.getCode());
            return retMsg;
        }
        String imagePath = redisTemplate.opsForValue().get(imageId);

        // cut image in proportion
        ArrayList<BufferedImage> images = cutImageInProportion(map, imagePath);

        StringBuilder sb;
        if (images != null && images.size() > 0) {
            Collections.reverse(images);
            sb = new StringBuilder();
            String accessToken;
            Object accessTokenRedis = redisTemplate.opsForValue().get("ai_ocr_access_token");
            if (accessTokenRedis != null) {
                accessToken = (String) accessTokenRedis;
            } else {
                accessToken = BaiduApiUtil.accessToken();
                redisTemplate.opsForValue().set("ai_ocr_access_token", accessToken, 24, TimeUnit.DAYS);
            }
            String fileName = FileUtil.getFileName(imagePath);
            String extension = FileUtil.getSuffix(fileName);
            for (int i = 0; i < images.size(); i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = BaiduApiUtil.getBufferedImageContext(images.get(i), extension, accessToken);
                sb.append(result + "\r\n");
            }
            retMsg.setCode(MessageCode.ImageRecognitionSuccess.getCode());
            retMsg.setMsg(MessageCode.ImageRecognitionSuccess.getDescription());
            retMsg.setData(sb);
        } else {
            retMsg.setCode(MessageCode.ImageRecognitionFail.getCode());
            retMsg.setMsg(MessageCode.ImageRecognitionFail.getDescription());
        }
        return retMsg;
    }

    private ArrayList<BufferedImage> cutImageInProportion(Map<String, Object> map, String imagePath) {
        int page = (Integer) map.get("page");
        int column = (Integer) map.get("column");
        ArrayList<Integer> yAxis = (ArrayList<Integer>) map.get("yAxis");
        int[] yIndex = new int[yAxis.size()];
        for (int i = 0; i < yAxis.size(); i++) {
            yIndex[i] = yAxis.get(i);
        }
        return ImageUtils.cutImage(page, column, yIndex, imagePath);
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
                return MessageCode.Image2BinaryFail.getDescription();
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
