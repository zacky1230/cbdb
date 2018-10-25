package com.chineseall.service;

import com.chineseall.entity.UploadPngTifInfo;
import com.chineseall.util.RetMsg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 12:06.
 */
public interface FileUploadService {
    /**
     * save single file
     *
     * @param file MultipartFile
     * @return RetMsg
     */
    RetMsg saveSingleFile(MultipartFile file);

    /**
     * save multi files
     *
     * @param files List
     * @return RetMsg
     */
    RetMsg saveMultiFile(List<MultipartFile> files);

    /**
     * get file real path by filename
     *
     * @param fileName String
     * @return String
     */
    String getRealFilePath(String fileName);

    /**
     * save tess training file
     *
     * @param file       file
     * @param lang       training language
     * @param fontFamily training fontFamily
     * @return UploadPngTifInfo
     */
    UploadPngTifInfo saveTrainTessFile(MultipartFile file, String lang, String fontFamily);

    /**
     * query file by timeStamp
     *
     * @param timeStamp timestamp
     * @return String
     */
    String getTifFilePathByTimeStamp(String timeStamp);

    /**
     * save image by ration
     *
     * @param file   file
     * @param type   type
     * @param width  width
     * @param height height
     * @return Map
     */
    Map<String, Object> saveOcrImage(MultipartFile file, String type, double width, double height);

    /**
     * save image with condition
     *
     * @param file      file
     * @param imageInfo imageInfo
     * @return Map
     */
    Map<String, Object> saveOcrImage(MultipartFile file, Map<String, Object> imageInfo);
}
