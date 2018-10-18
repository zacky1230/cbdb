package com.chineseall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 09:43.
 */
public class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);


    /**
     * 使用 BufferedImage 获取图片大小
     *
     * @param src
     */
    public static Map<String, Object> getImageSizeByBufferedImage(String src) {
        long beginTime = System.currentTimeMillis();
        Map<String, Object> imageInfoMap = new HashMap<>(1);
        File file = new File(src);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        BufferedImage sourceImg;
        try {
            sourceImg = ImageIO.read(is);
            imageInfoMap.put("IMAGE_SIZE", file.length());
            imageInfoMap.put("IMAGE_WIDTH", sourceImg.getWidth());
            imageInfoMap.put("IMAGE_HEIGHT", sourceImg.getHeight());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        logger.info("使用[BufferedImage]获取图片尺寸耗时：[" + (endTime - beginTime) + "]ms");
        return imageInfoMap;
    }

    /**
     * 使用ImageReader获取图片尺寸
     *
     * @param src 源图片路径
     */
    public static Map<String, Object> getImageSizeByImageReader(String src) {
        long beginTime = System.currentTimeMillis();
        Map<String, Object> imageInfoMap = new HashMap<>(1);
        File file = new File(src);
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(file.getName().split("\\.")[1]);
            ImageReader reader = readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            reader.setInput(iis, true);
            imageInfoMap.put("IMAGE_SIZE", file.length());
            imageInfoMap.put("IMAGE_WIDTH", reader.getWidth(0));
            imageInfoMap.put("IMAGE_HEIGHT", reader.getHeight(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        logger.info("使用[ImageReader]获取图片尺寸耗时：[" + (endTime - beginTime) + "]ms");
        return imageInfoMap;
    }

    /**
     * 等比例切割图片
     *
     * @param page
     * @param column
     * @param xAxes
     * @param imagePath
     * @return
     */
    public static ArrayList<BufferedImage> cutImage(int page, int column, int[] xAxes, String imagePath) {

        ArrayList<BufferedImage> images = new ArrayList<>();
        if (StringUtils.isEmpty(imagePath)) {
            return images;
        }

        if ((page * 2) != xAxes.length) {
            return images;
        }
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            if (page == 1) {
                getBufferedImage(images, image, column, xAxes[0], xAxes[1]);
            } else {
                getBufferedImage(images, image, column / 2, xAxes[0], xAxes[1]);
                getBufferedImage(images, image, column / 2, xAxes[2], xAxes[3]);
            }
        } catch (Exception e) {

        }
        return images;
    }

    /**
     * 切割图片
     *
     * @param src
     * @param rows
     * @param cols
     * @return
     */
    public static ArrayList<BufferedImage> cutImage(String src, int rows, int cols, int xIndex, int yIndex,
                                                    int gapStart, int gapEnd, int rowsRight, int columnRight, int
                                                            yIndexRigth, int heigth) {
        ArrayList<BufferedImage> list = new ArrayList<>();

        try {
            BufferedImage img = ImageIO.read(new File(src));
            BigDecimal b1 = new BigDecimal(Double.toString(heigth));
            BigDecimal b2 = new BigDecimal(Double.toString(img.getHeight()));
            double rate = b1.divide(b2, 2).doubleValue();
            /**
             * if picture changed, fixed.
             */
            if (rate != 1.0) {
                img = resizeImage(img, rate * 100 / 100);
            }
            if (gapStart == 0 && gapEnd == 0 && columnRight == 0) {
                return getBufferedImage(list, img, rows, cols, xIndex, yIndex);
            } else {
                BufferedImage leftImage = img.getSubimage(xIndex, yIndex, gapStart, (img.getHeight() - yIndex));
                BufferedImage rightImage = img.getSubimage(gapEnd, yIndexRigth, (img.getWidth() - gapEnd), (img
                        .getHeight() - yIndexRigth));
                /**
                 * 左边
                 */
                {
                    ImageIO.write(leftImage, "png", new File("/Users/zacky/Desktop/leftImage.png"));
                    getBufferedImage(list, leftImage, rows, cols, xIndex, yIndex);
                }
                /**
                 * 右边
                 */
                {
                    ImageIO.write(rightImage, "png", new File("/Users/zacky/Desktop/rightImage.png"));
                    getBufferedImage(list, rightImage, rowsRight, columnRight, 0, yIndexRigth);
                }
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ArrayList<BufferedImage> getBufferedImage(ArrayList<BufferedImage> images, BufferedImage image, int column, int startY, int endY) {
        int imageHeigth = image.getHeight();
        int lw = (endY - startY) / column;
        for (int i = 0; i < column; i++) {
            BufferedImage bufferedImage = image.getSubimage(startY + (i * lw), 0, lw, imageHeigth);
            images.add(bufferedImage);
        }
        return images;
    }

    private static ArrayList<BufferedImage> getBufferedImage(ArrayList<BufferedImage> list, BufferedImage img, int
            rows, int cols, int xIndex, int yIndex) {
        int lw = (img.getWidth() - xIndex) / cols;
        int lh = (img.getHeight() - yIndex) / rows;
        for (int i = 0; i < rows * cols; i++) {
            BufferedImage buffImg = img.getSubimage(xIndex + (i % cols * lw), yIndex + (i / cols * lh), lw, lh);
            list.add(buffImg);
        }
        return list;
    }

    /**
     * 对图片进行缩放
     *
     * @param originalImage 原始图片
     * @param times         放大倍数
     * @return
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, double times) {
        int width = (int) (originalImage.getWidth() * times);
        int height = (int) (originalImage.getHeight() * times);

        int tType = originalImage.getType();
        if (0 == tType) {
            tType = 5;
        }
        BufferedImage newImage = new BufferedImage(width, height, tType);
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return newImage;
    }

    public static void main(String[] args) throws IOException {
//        String filePath = "/Users/zacky/Desktop/s0110023_0004.png";
        String filePath = "/Users/zacky/Desktop/test.png";

        ArrayList<BufferedImage> biLists = cutImage(filePath, 1, 6, 6, 0, 341, 370, 1, 6, 0, 100);
        String fileNameString = "/Users/zacky/Desktop";
        int number = 0;
        String format = "png";
        for (BufferedImage bi : biLists) {
            File file1 = new File(fileNameString + File.separator + number + "." + format);
            ImageIO.write(bi, format, file1);
            number++;
        }
    }


}
