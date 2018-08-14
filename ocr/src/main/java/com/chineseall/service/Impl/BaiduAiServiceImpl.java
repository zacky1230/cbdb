package com.chineseall.service.Impl;

import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.service.BaiduAiService;
import com.chineseall.util.BaiduApiUtil;
import com.chineseall.util.FileUtil;
import com.chineseall.util.PictureUtils;
import com.chineseall.util.RetMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:44.
 */
@Service("baiduAiServiceImpl")
public class BaiduAiServiceImpl implements BaiduAiService {
    Logger logger = LoggerFactory.getLogger(BaiduAiServiceImpl.class);

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public RetMsg getContext(int id) {
        RetMsg retMsg = new RetMsg();
        UploadFileInfo uploadFileInfo = fileUploadServiceDao.queryById(id);
        if (uploadFileInfo != null) {
            String filePath = uploadFileInfo.getFileUploadPath();

            /**
             * 获取accessToken
             */
            String accessToken;
            Object accessTokenRedis = redisTemplate.opsForValue().get("ai_ocr_access_token");
            if (accessTokenRedis != null) {
                accessToken = (String) accessTokenRedis;
            } else {
                accessToken = BaiduApiUtil.accessToken();
                redisTemplate.opsForValue().set("ai_ocr_access_token", accessToken, 24, TimeUnit.DAYS);
            }

            String result = BaiduApiUtil.getImageContext(filePath, accessToken);
            logger.info(result);
            retMsg.setData(result);
        }
        return retMsg;
    }

    /**
     * filePath 上传文件路径
     * xIndex 起点横坐标
     * yIndex 起点纵坐标
     * row 行数
     * column 列数
     * gapStart gap 起始
     * gapEnd gap 结束
     * columnRight 列数右边
     *
     * @param map
     * @return
     */
    @Override
    public RetMsg getSplitContext(Map map) {
        RetMsg retMsg = new RetMsg();
        if (map != null) {
            String filePath = (String) map.get("filePath");
            int xIndex = Integer.parseInt((String) map.get("xIndex"));
            int yIndex = Integer.parseInt((String) map.get("yIndex"));
            int row = (int) map.get("row");
            int column = Integer.parseInt((String) map.get("column"));
            int gapStart = Integer.parseInt((String) map.get("gapStart"));
            int gapEnd = Integer.parseInt((String) map.get("gapEnd"));
            int rowsRight = (int) map.get("rowsRight");
            int columnRight = Integer.parseInt((String) map.get("columnRight"));
            int yIndexRigth = Integer.parseInt((String) map.get("yIndexRigth"));
            int height = Integer.parseInt((String) map.get("height"));

            File file = new File(filePath);

            ArrayList<BufferedImage> bufferedImages = PictureUtils.cutImage(filePath, row, column, xIndex, yIndex,
                    gapStart, gapEnd, rowsRight, columnRight, yIndexRigth, height);
            String format = FileUtil.getSuffix(file.getName());
            String accessToken;
            Object accessTokenRedis = redisTemplate.opsForValue().get("ai_ocr_access_token");
            if (accessTokenRedis != null) {
                accessToken = (String) accessTokenRedis;
            } else {
                accessToken = BaiduApiUtil.accessToken();
                redisTemplate.opsForValue().set("ai_ocr_access_token", accessToken, 24, TimeUnit.DAYS);
            }
            StringBuilder sb = new StringBuilder();
            if (bufferedImages != null && bufferedImages.size() > 0) {
                Collections.reverse(bufferedImages);
                for (int i = 0; i < bufferedImages.size(); i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String result = BaiduApiUtil.getBufferedImageContext(bufferedImages.get(i), format, accessToken);
                    sb.append(result);
                    sb.append("\r\n");
                }
            }
            retMsg.setData(sb.toString());
        }
        return retMsg;
    }
}
