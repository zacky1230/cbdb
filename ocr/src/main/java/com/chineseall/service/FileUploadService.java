package com.chineseall.service;

import com.chineseall.util.RetMsg;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 12:06.
 */
public interface FileUploadService {
    RetMsg saveSingleFile(MultipartFile file);

    RetMsg saveMultiFile(List<MultipartFile> files);

    String getRealFilePath(String fileName);

    String saveMutilTessFile(List<MultipartFile> files, String lang, String fontFamily);
}
