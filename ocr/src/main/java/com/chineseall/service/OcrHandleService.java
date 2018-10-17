package com.chineseall.service;

import com.chineseall.util.RetMsg;

import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:07.
 */
public interface OcrHandleService {

    RetMsg ocrImageHandle(Map<String, Object> map);

    RetMsg imageRecognition(Map<String, Object> map);
}
