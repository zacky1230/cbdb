package com.chineseall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.config.WebSocketServer;
import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileContext;
import com.chineseall.service.FileUploadService;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.*;
import com.chineseall.util.entity.ImageOrdinate;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:08.
 */
@Service
public class OcrHandleServiceImpl implements OcrHandleService {
    private Logger logger = LoggerFactory.getLogger(OcrHandleServiceImpl.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

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
        RetMsg<JSONObject> retMsg = new RetMsg<>();
        String realPath = (String) map.get("filePath");
        try {
            WebSocketServer.sendInfo("正在图片二值化操作");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resultImgPath = picture2Binary(realPath);
        try {
            WebSocketServer.sendInfo("正在检验图片");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = (new Double((Double) map.get("height"))).intValue();

        int[] yAxes = detectLines(resultImgPath, detectHorizonLine, 0, height);
        int[] xAxes = detectLines(resultImgPath, detectVerticalLine, yAxes[0], yAxes[1]);
        JSONObject json = new JSONObject();
        String imageId = (String) map.get("imageId");
        json.put("xAxes", xAxes);
        json.put("yAxes", yAxes);
        json.put("imageId", imageId);
        json.put("imageInfo", getHandlerImage(imageId));
        retMsg.setMsg(MessageCode.ImageUploadSuccess.getDescription());
        retMsg.setCode(MessageCode.ImageUploadSuccess.getCode());
        retMsg.setData(json);
        return retMsg;
    }

    @Override
    public RetMsg imageRecognition(Map<String, Object> map) {
        RetMsg<StringBuilder> retMsg = new RetMsg<>();
        String imageId = (String) map.get("imageId");
        if (StringUtils.isEmpty(imageId)) {
            retMsg.setCode(MessageCode.ImageFormatError.getCode());
            retMsg.setMsg(MessageCode.ImageNotFound.getDescription());
            return retMsg;
        }
        String imagePath = redisTemplate.opsForValue().get(imageId);

        // cut image by coordinate
        ArrayList ordinates = (ArrayList) map.get("coordinate");
        List<ImageOrdinate> imageOrdinates = new ArrayList<>();

        if (imageOrdinates == null || imageOrdinates.size() < 0) {
            retMsg.setCode(MessageCode.ImageRecognitionFail.getCode());
            retMsg.setMsg(MessageCode.ImageRecognitionFail.getDescription());
            return retMsg;
        }

        for (Object map1 : ordinates) {
            ImageOrdinate imageOrdinate = BeanUtil.map2Bean((Map<String, Object>) map1, ImageOrdinate.class);
            imageOrdinates.add(imageOrdinate);
        }

        imageOrdinates = imageOrdinates.stream().sorted(Comparator.comparing(ImageOrdinate::getTopX)).collect
                (Collectors.toList());

        ArrayList<BufferedImage> images = ImageUtils.cutImage(imagePath, imageOrdinates);

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

        String imagePath = redisTemplate.opsForValue().get(imageId);

        retMsg.setData(getImageBase64(imagePath));
        retMsg.setCode(MessageCode.ImageLoadSuccess.getCode());
        retMsg.setMsg(MessageCode.ImageLoadSuccess.getDescription());
        return retMsg;
    }

    private String getImageBase64(String imagePath) {
        InputStream in;
        byte[] data = new byte[0];
        try {
            assert imagePath != null;
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    @Override
    public RetMsg imageUpload(MultipartFile file, String type, double width, double height) {
        RetMsg retMsg = new RetMsg();
        if (file.getContentType() != null && !file.getContentType().contains("image")) {
            retMsg.setCode(MessageCode.ImageFormatError.getCode());
            retMsg.setMsg(MessageCode.ImageFormatError.getDescription());
            return retMsg;
        }
        try {
            WebSocketServer.sendInfo("正在保存图片");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = fileUploadService.saveOcrImage(file, type, width, height);

        if (MessageCode.ImageUploadFail.getCode() == (int) map.get("code")) {
            retMsg.fail(MessageCode.ImageUploadFail.getDescription());
        } else {
            map.put("type", type);
            map.put("width", width);
            map.put("height", height);
            retMsg = ocrImageHandle(map);
        }
        return retMsg;
    }

    @Override
    public RetMsg saveImageInfo(String imageId, UploadFileContext info) {
        RetMsg retMsg = new RetMsg();
        info.setFileId(imageId);
        int result = fileUploadServiceDao.updateImageInfo(info);
        if (result > 0) {
            retMsg.setCode(MessageCode.ImageInfoSaveSuccess.getCode());
            retMsg.setMsg(MessageCode.ImageInfoSaveSuccess.getDescription());
        } else {
            retMsg.setCode(MessageCode.ImageInfoSaveFail.getCode());
            retMsg.setMsg(MessageCode.ImageNotFound.getDescription());
        }
        return retMsg;
    }

    @Override
    public RetMsg queryImageInfo(int id) {
        RetMsg retMsg = new RetMsg();
        UploadFileContext uploadFileContext = fileUploadServiceDao.queryImageInfoById(id);
        retMsg.setCode(MessageCode.QuerySuccess.getCode());
        retMsg.setMsg(MessageCode.QuerySuccess.getDescription());
        uploadFileContext.setImageInfo(getImageBase64(uploadFileContext.getFilePath()));
        retMsg.setData(uploadFileContext);
        return retMsg;
    }


    private int[] detectLines(String resultImgPath, String url, int start, int end) {
        int[] axes = {};
        JSONObject jsonObject;
        try {
            if (StringUtils.isEmpty(resultImgPath)) {
                return axes;
            }
            url = url.replace("FilePath", resultImgPath).replace("Start", String.valueOf(start)).replace("End",
                    String.valueOf(end));
            jsonObject = HttpClientUtil.httpGet(url);
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
