package com.chineseall.service;

import com.chineseall.entity.UploadFileContext;
import com.chineseall.util.RetMsg;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:07.
 */
public interface OcrHandleService {

    RetMsg ocrImageHandle(Map<String, Object> map);

    RetMsg imageRecognition(Map<String, Object> map);

    RetMsg getHandlerImage(String imageId);

    RetMsg imageUpload(MultipartFile file, String type, double width, double height);

    RetMsg saveImageInfo(String imageId, UploadFileContext info);

    RetMsg queryImageInfo(int id);
}
