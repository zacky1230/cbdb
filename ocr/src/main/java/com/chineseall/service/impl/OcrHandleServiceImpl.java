package com.chineseall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.FileUploadService;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    protected FileUploadService fileUploadService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${cv.image2Binary}")
    private String image2Binary;

    @Value("${cv.vertical.line.detect}")
    private String detectVerticalLine;

    @Value("${cv.horizon.line.detect}")
    private String detectHorizonLine;

    @Override
    public RetMsg ocrImageHandle(Map<String, Object> map) {
        RetMsg retMsg = new RetMsg();
        String realPath = (String) map.get("filePath");
        String page = (String) map.get("page");
        String resultImgPath = picture2Binary(realPath);
        int xAxes[] = detectLines(resultImgPath, page, detectVerticalLine);
        int yAxes[] = detectLines(resultImgPath, page, detectHorizonLine);
        JSONObject json = new JSONObject();
        json.put("xAxes", xAxes);
        json.put("yAxes", yAxes);
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
                if (i == images.size() - 1) {
                    sb.append(result);
                } else {
                    sb.append(result).append("\r\n");
                }

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

    @Override
    public RetMsg getHandlerImage(String imageId) {
        RetMsg retMsg = new RetMsg();
        InputStream in;
        byte[] data;
        try {
            String imagePath = redisTemplate.opsForValue().get(imageId);
            assert imagePath != null;
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            retMsg.setData(null);
            retMsg.setCode(MessageCode.ImageNotFound.getCode());
            retMsg.setMsg(MessageCode.ImageNotFound.getDescription());
            return retMsg;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        retMsg.setData(encoder.encode(data));
        retMsg.setCode(MessageCode.ImageLoadSuccess.getCode());
        retMsg.setMsg(MessageCode.ImageLoadSuccess.getDescription());
        return retMsg;
    }

    @Override
    public RetMsg imageUpload(MultipartFile file, String type, String page) {
        RetMsg retMsg = new RetMsg();
        if (!file.getContentType().contains("image")) {
            retMsg.setCode(MessageCode.ImageFormatError.getCode());
            retMsg.setMsg(MessageCode.ImageFormatError.getDescription());
            return retMsg;
        }
        Map<String, Object> map = fileUploadService.saveOcrImage(file, type);

        if (MessageCode.ImageUploadFail.getCode() == (int) map.get("code")) {
            retMsg.fail(MessageCode.ImageUploadFail.getDescription());
        } else {
            map.put("type", type);
            map.put("page", page);
            retMsg = ocrImageHandle(map);
        }
        return retMsg;
    }

    private ArrayList<BufferedImage> cutImageInProportion(Map<String, Object> map, String imagePath) {
        int page = (Integer) map.get("page");
        int column = (Integer) map.get("column");
        ArrayList<Integer> xAxes = (ArrayList<Integer>) map.get("xAxes");
        int[] xAxesArr = new int[xAxes.size()];
        for (int i = 0; i < xAxes.size(); i++) {
            xAxesArr[i] = xAxes.get(i);
        }
        return ImageUtils.cutImage(page, column, xAxesArr, imagePath);
    }

    private int[] detectLines(String resultImgPath, String page, String url) {
        int[] axes = {};
        JSONObject jsonObject;
        try {
            if (StringUtils.isEmpty(resultImgPath) && StringUtils.isEmpty(page)) {
                return axes;
            }
            jsonObject = HttpClientUtil.httpGet(url.replace("FilePath", resultImgPath).replace("Page",
                    page));
            logger.info(String.format("The axes of the image is : %s", jsonObject.toJSONString()));
            String data = jsonObject.getString("data");
            String[] axesData = data.substring(1, data.length() - 1).split(",");
            axes = new int[axesData.length];
            for (int i = 0; i < axesData.length; i++) {
                axes[i] = Integer.parseInt(axesData[i]);
            }
            return axes;
        } catch (Exception e) {
            return axes;
        }
    }


    private String picture2Binary(String realPath) {
        String filePath = FileUtil.getParentDirectory(realPath);
        String fileName = FileUtil.getFileName(realPath);
        JSONObject jsonObject;
        try {
            if (StringUtils.isEmpty(fileName) && StringUtils.isEmpty(filePath)) {
                return MessageCode.Image2BinaryFail.getDescription();
            }
            assert filePath != null;
            assert fileName != null;
            jsonObject = HttpClientUtil.httpGet(image2Binary.replace("FilePath", filePath).replace("FileName",
                    fileName));
            logger.info(String.format("The image %s binary success, the save info is %s", realPath, jsonObject
                    .toJSONString()));
            return jsonObject.get("imgPath").toString();
        } catch (Exception e) {
            return "fail";
        }

    }

}
