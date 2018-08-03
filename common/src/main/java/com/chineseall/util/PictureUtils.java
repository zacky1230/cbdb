package com.chineseall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 09:43.
 */
public class PictureUtils {

    private static Logger logger = LoggerFactory.getLogger(PictureUtils.class);

    public static Map<String, Object> getImageInfo(String filePath) {

        long startTime = System.currentTimeMillis();
        Map<String, Object> imageInfoMap = new HashMap<>();
        InputStream is = null;
        try {
            File file = new File(filePath);
            is = new FileInputStream(file);
            BufferedImage image = ImageIO.read(is);
            imageInfoMap.put("IMAGE_SIZE", file.length());
            imageInfoMap.put("IMAGE_WIDTH", image.getWidth());
            imageInfoMap.put("IMAGE_HEIGHT", image.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close(); // 关闭流
                } catch (IOException e) {
                    logger.error("getImageInfo I/O exception " + e.getMessage(), e);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("take time: " + (endTime - startTime));
        return imageInfoMap;
    }

    public static void main(String[] args) {
        String filePath = "/Users/zacky/Desktop/2013120803a.png";
        Map<String, Object> map = getImageInfo(filePath);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }
}
