package com.chineseall.service.Impl;

import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.service.BaiduAiService;
import com.chineseall.util.BaiduApiUtil;
import com.chineseall.util.FileUtil;
import com.chineseall.util.PictureUtils;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:44.
 */
@Service("baiduAiServiceImpl")
public class BaiduAiServiceImpl implements BaiduAiService {

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

    @Override
    public RetMsg getContext(int id) {
        UploadFileInfo uploadFileInfo = fileUploadServiceDao.queryById(id);
        if (uploadFileInfo != null) {
            String filePath = uploadFileInfo.getFileUploadPath();
            String accessToken = BaiduApiUtil.accessToken();
            String result = BaiduApiUtil.getImageContext(filePath, accessToken);
            System.out.println(result);
        }
        return null;
    }

    /**
     * filePath 上传文件路径
     * xIndex 起点横坐标
     * yIndex 起点纵坐标
     * row 行数
     * column 列数
     *
     * @param map
     * @return
     */
    @Override
    public RetMsg getSplitContext(Map map) {
        if (map != null) {
            String filePath = (String) map.get("filePath");
            int xIndex = (int) map.get("xIndex");
            int yIndex = (int) map.get("yIndex");
            int row = (int) map.get("row");
            int column = (int) map.get("column");

            File file = new File(filePath);
            String directory = file.getParent();

            ArrayList<BufferedImage> bufferedImages = PictureUtils.cutImage(filePath, row, column, row * column, xIndex,
                    yIndex);
            String format = FileUtil.getSuffix(file.getName());
            String accessToken = "";
            StringBuilder sb = new StringBuilder();
            if (bufferedImages != null && bufferedImages.size() > 0) {
                for (int i = 0; i < bufferedImages.size(); i++) {
                    String result = BaiduApiUtil.getBufferedImageContext(bufferedImages.get(i), format, accessToken);
                    sb.append(result);
                    sb.append("\r\n");
                }
            }
        }
        return null;
    }
}
