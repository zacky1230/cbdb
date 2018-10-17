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
    RetMsg saveSingleFile(MultipartFile file);

    RetMsg saveMultiFile(List<MultipartFile> files);

    String getRealFilePath(String fileName);

    UploadPngTifInfo saveTrainTessFile(MultipartFile file, String lang, String fontFamily);

    String getTifFilePathByTimeStamp(String timeStamp);

    Map<String, Object> saveOcrImage(MultipartFile file, String type);
}
